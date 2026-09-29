package com.televet.service;

import com.televet.model.*;
import com.televet.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TeleVetService {
    private final ContactRepository contacts;
    private final ProductRepository products;
    private final ChartOfAccountRepository accounts;
    private final ConsultationRepository consultations;
    private final InvoiceRepository invoices;
    private final VendorBillRepository bills;
    private final PaymentRepository payments;
    private final BudgetRepository budgets;
    private final JournalEntryRepository journals;

    public TeleVetService(ContactRepository contacts, ProductRepository products, ChartOfAccountRepository accounts,
                           ConsultationRepository consultations, InvoiceRepository invoices, VendorBillRepository bills,
                           PaymentRepository payments, BudgetRepository budgets, JournalEntryRepository journals) {
        this.contacts=contacts; this.products=products; this.accounts=accounts; this.consultations=consultations;
        this.invoices=invoices; this.bills=bills; this.payments=payments; this.budgets=budgets; this.journals=journals;
    }

    public List<Contact> contacts(){return contacts.findAll();}
    public Contact contact(Long id){return contacts.findById(id).orElseThrow();}
    public Contact saveContact(Contact c){return contacts.save(c);}
    public void deleteContact(Long id){contacts.deleteById(id);}

    public List<Product> products(){return products.findAll();}
    public Product saveProduct(Product p){return products.save(p);}
    public void deleteProduct(Long id){products.deleteById(id);}

    public List<ChartOfAccount> accounts(){return accounts.findAll();}
    public ChartOfAccount saveAccount(ChartOfAccount a){return accounts.save(a);}

    @Transactional
    public Consultation saveConsultation(Consultation c){
        if(c.getStatus()==null) c.setStatus("SCHEDULED");
        if(c.getPriority()==null || c.getPriority().isBlank()) c.setPriority(calculatePriority(c.getSymptoms()));
        return consultations.save(c);
    }

    public String calculatePriority(String symptoms){
        if(symptoms==null) return "LOW";
        String s=symptoms.toLowerCase();
        if(s.contains("unable to stand") || s.contains("severe bleeding") || s.contains("collapse") || s.contains("breathing difficulty")) return "CRITICAL";
        if(s.contains("high fever") || s.contains("severe") || s.contains("blood") || s.contains("not eating")) return "HIGH";
        if(s.contains("fever") || s.contains("swelling") || s.contains("pain")) return "MEDIUM";
        return "LOW";
    }

    public List<Consultation> consultations(){return consultations.findAll();}
    public Consultation consultation(Long id){return consultations.findById(id).orElseThrow();}

    @Transactional
    public Invoice createInvoice(Invoice i){
        BigDecimal c=i.getConsultationAmount()==null?BigDecimal.ZERO:i.getConsultationAmount();
        BigDecimal m=i.getMedicineAmount()==null?BigDecimal.ZERO:i.getMedicineAmount();
        i.setTotalAmount(c.add(m));
        if(i.getInvoiceNumber()==null || i.getInvoiceNumber().isBlank()) i.setInvoiceNumber("INV-"+System.currentTimeMillis());
        Invoice saved=invoices.save(i);
        journals.save(new JournalEntry("SALES","CUSTOMER_INVOICE",saved.getId(),"Customer Receivable","Tele-Consultation Revenue",c));
        if(m.signum()>0) journals.save(new JournalEntry("SALES","CUSTOMER_INVOICE",saved.getId(),"Customer Receivable","Pharmacy Sales",m));
        return saved;
    }

    public List<Invoice> invoices(){return invoices.findAll();}

    @Transactional
    public VendorBill createBill(VendorBill b){
        if(b.getBillNumber()==null || b.getBillNumber().isBlank()) b.setBillNumber("BILL-"+System.currentTimeMillis());
        VendorBill saved=bills.save(b);
        journals.save(new JournalEntry("PURCHASE","VENDOR_BILL",saved.getId(),"Lab Processing Expenses","Pharma Lab Payables",saved.getAmount()));
        return saved;
    }

    public List<VendorBill> bills(){return bills.findAll();}

    @Transactional
    public Payment makePayment(Payment p){
        Payment saved=payments.save(p);
        if("CUSTOMER_RECEIPT".equalsIgnoreCase(p.getPaymentType()) && p.getInvoiceId()!=null){
            Invoice i=invoices.findById(p.getInvoiceId()).orElseThrow();
            i.setPaymentStatus("PAID"); invoices.save(i);
            journals.save(new JournalEntry(p.getMethod().equalsIgnoreCase("BANK")?"BANK":"CASH","CUSTOMER_RECEIPT",i.getId(),
                    p.getMethod().equalsIgnoreCase("BANK")?"Bank":"Cash","Customer Receivable",p.getAmount()));
        } else if("VENDOR_PAYMENT".equalsIgnoreCase(p.getPaymentType()) && p.getVendorBillId()!=null){
            VendorBill b=bills.findById(p.getVendorBillId()).orElseThrow();
            b.setPaymentStatus("PAID"); bills.save(b);
            journals.save(new JournalEntry("BANK","VENDOR_PAYMENT",b.getId(),"Pharma Lab Payables",
                    p.getMethod().equalsIgnoreCase("BANK")?"Bank":"Cash",p.getAmount()));
        }
        return saved;
    }

    public List<Payment> payments(){return payments.findAll();}
    public List<Budget> budgets(){return budgets.findAll();}
    public Budget saveBudget(Budget b){return budgets.save(b);}

    public List<JournalEntry> journals(){return journals.findAll();}

    public java.util.Map<String,Object> dashboard(){
        BigDecimal revenue=invoices.findAll().stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal expenses=bills.findAll().stream().map(VendorBill::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal received=payments.findAll().stream().filter(p->"CUSTOMER_RECEIPT".equalsIgnoreCase(p.getPaymentType()))
                .map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal paid=payments.findAll().stream().filter(p->"VENDOR_PAYMENT".equalsIgnoreCase(p.getPaymentType()))
                .map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        return java.util.Map.of(
                "farmers", contacts.findAll().stream().filter(c->"FARMER".equalsIgnoreCase(c.getContactType())).count(),
                "specialists", contacts.findAll().stream().filter(c->"SPECIALIST".equalsIgnoreCase(c.getContactType())).count(),
                "consultations", consultations.count(),
                "invoices", invoices.count(),
                "vendorBills", bills.count(),
                "revenue", revenue,
                "labExpenses", expenses,
                "cashReceived", received,
                "vendorPayments", paid,
                "netResult", revenue.subtract(expenses)
        );
    }

    public java.util.Map<String,Object> reports(){
        BigDecimal revenue=invoices.findAll().stream().map(Invoice::getTotalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal labCosts=bills.findAll().stream().map(VendorBill::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal pharmacyRevenue=invoices.findAll().stream().map(Invoice::getMedicineAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal consultationRevenue=invoices.findAll().stream().map(Invoice::getConsultationAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal cash=payments.findAll().stream().filter(p->"CUSTOMER_RECEIPT".equalsIgnoreCase(p.getPaymentType()))
                .map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add)
                .subtract(payments.findAll().stream().filter(p->"VENDOR_PAYMENT".equalsIgnoreCase(p.getPaymentType()))
                .map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        BigDecimal allocated=budgets.findAll().stream().map(Budget::getAllocatedAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal actual=budgets.findAll().stream().map(Budget::getActualAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        return java.util.Map.of(
                "profitAndLoss", java.util.Map.of("consultationRevenue",consultationRevenue,"pharmacyRevenue",pharmacyRevenue,
                        "totalRevenue",revenue,"labProcessingExpenses",labCosts,"netResult",revenue.subtract(labCosts)),
                "balanceSheet", java.util.Map.of("cashOrBankNet",cash,"pharmaLabPayables",labCosts.subtract(
                        payments.findAll().stream().filter(p->"VENDOR_PAYMENT".equalsIgnoreCase(p.getPaymentType()))
                        .map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add))),
                "budget", java.util.Map.of("allocated",allocated,"actual",actual,
                        "utilizationPercent",allocated.signum()==0?BigDecimal.ZERO:actual.multiply(BigDecimal.valueOf(100)).divide(allocated,2,java.math.RoundingMode.HALF_UP))
        );
    }
}
