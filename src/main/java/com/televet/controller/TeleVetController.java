package com.televet.controller;

import com.televet.model.*;
import com.televet.service.TeleVetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TeleVetController {
    private final TeleVetService service;
    public TeleVetController(TeleVetService service){this.service=service;}

    @GetMapping("/dashboard") public Map<String,Object> dashboard(){return service.dashboard();}
    @GetMapping("/reports") public Map<String,Object> reports(){return service.reports();}

    @GetMapping("/contacts") public List<Contact> contacts(){return service.contacts();}
    @GetMapping("/contacts/{id}") public Contact contact(@PathVariable Long id){return service.contact(id);}
    @PostMapping("/contacts") @ResponseStatus(HttpStatus.CREATED) public Contact contact(@RequestBody Contact c){return service.saveContact(c);}
    @PutMapping("/contacts/{id}") public Contact updateContact(@PathVariable Long id,@RequestBody Contact c){c.setId(id);return service.saveContact(c);}
    @DeleteMapping("/contacts/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteContact(@PathVariable Long id){service.deleteContact(id);}

    @GetMapping("/products") public List<Product> products(){return service.products();}
    @PostMapping("/products") @ResponseStatus(HttpStatus.CREATED) public Product product(@RequestBody Product p){return service.saveProduct(p);}
    @PutMapping("/products/{id}") public Product updateProduct(@PathVariable Long id,@RequestBody Product p){p.setId(id);return service.saveProduct(p);}
    @DeleteMapping("/products/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteProduct(@PathVariable Long id){service.deleteProduct(id);}

    @GetMapping("/accounts") public List<ChartOfAccount> accounts(){return service.accounts();}
    @PostMapping("/accounts") @ResponseStatus(HttpStatus.CREATED) public ChartOfAccount account(@RequestBody ChartOfAccount a){return service.saveAccount(a);}

    @PostMapping("/televet/consultations") @ResponseStatus(HttpStatus.CREATED)
    public Consultation consultation(@RequestBody Consultation c){return service.saveConsultation(c);}
    @GetMapping("/televet/consultations") public List<Consultation> consultations(){return service.consultations();}
    @GetMapping("/televet/consultations/{id}") public Consultation consultation(@PathVariable Long id){return service.consultation(id);}
    @PutMapping("/televet/consultations/{id}") public Consultation updateConsultation(@PathVariable Long id,@RequestBody Consultation c){c.setId(id);return service.saveConsultation(c);}

    @PostMapping("/invoices") @ResponseStatus(HttpStatus.CREATED)
    public Invoice invoice(@RequestBody Invoice i){return service.createInvoice(i);}
    @GetMapping("/invoices") public List<Invoice> invoices(){return service.invoices();}

    @PostMapping("/vendor-bills") @ResponseStatus(HttpStatus.CREATED)
    public VendorBill bill(@RequestBody VendorBill b){return service.createBill(b);}
    @GetMapping("/vendor-bills") public List<VendorBill> bills(){return service.bills();}

    @PostMapping("/payments") @ResponseStatus(HttpStatus.CREATED)
    public Payment payment(@RequestBody Payment p){return service.makePayment(p);}
    @GetMapping("/payments") public List<Payment> payments(){return service.payments();}

    @GetMapping("/budgets") public List<Budget> budgets(){return service.budgets();}
    @PostMapping("/budgets") @ResponseStatus(HttpStatus.CREATED) public Budget budget(@RequestBody Budget b){return service.saveBudget(b);}
    @PutMapping("/budgets/{id}") public Budget updateBudget(@PathVariable Long id,@RequestBody Budget b){b.setId(id);return service.saveBudget(b);}

    @GetMapping("/journals") public List<JournalEntry> journals(){return service.journals();}
}
