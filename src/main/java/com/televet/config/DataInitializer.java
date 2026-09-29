package com.televet.config;

import com.televet.model.*;
import com.televet.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(ContactRepository contacts, ProductRepository products, ChartOfAccountRepository accounts) {
        return args -> {
            if (contacts.count()==0) {
                Contact farmer=new Contact(); farmer.setName("Sample Farmer"); farmer.setContactType("FARMER");
                farmer.setPhone("9876543210"); farmer.setRegion("Regional Tele-Vet Hub 2"); contacts.save(farmer);

                Contact doctor=new Contact(); doctor.setName("Dr. Ananya Kumar"); doctor.setContactType("SPECIALIST");
                doctor.setPhone("9000000001"); doctor.setEmail("doctor@televet.local"); contacts.save(doctor);

                Contact lab=new Contact(); lab.setName("AgriVet Pharma Lab"); lab.setContactType("LAB_VENDOR");
                lab.setPhone("9000000002"); lab.setEmail("lab@agrivet.local"); contacts.save(lab);
            }
            if(products.count()==0){
                Product p1=new Product(); p1.setName("Tele-Veterinary Consultation"); p1.setProductType("SERVICE"); p1.setCategory("CONSULTATION"); p1.setPrice(new BigDecimal("500")); products.save(p1);
                Product p2=new Product(); p2.setName("Veterinary Antibiotic Kit"); p2.setProductType("GOODS"); p2.setCategory("MEDICINE"); p2.setPrice(new BigDecimal("850")); p2.setStockQuantity(25); products.save(p2);
                Product p3=new Product(); p3.setName("Lab Diagnostic Test"); p3.setProductType("SERVICE"); p3.setCategory("LAB_TEST"); p3.setPrice(new BigDecimal("300")); products.save(p3);
            }
            if(accounts.count()==0){
                String[][] rows={{"1000","Diagnostic Equipment","ASSET"},{"1010","Cash/Bank","ASSET"},{"2000","Pharma Lab Payables","LIABILITY"},{"2010","Specialist Creditors","LIABILITY"},{"4000","Tele-Consultation Revenue","INCOME"},{"4010","Pharmacy Sales","INCOME"},{"5000","Lab Processing Expenses","EXPENSE"},{"5010","Medical Supply Costs","EXPENSE"}};
                for(String[] r:rows){ ChartOfAccount a=new ChartOfAccount(); a.setCode(r[0]); a.setName(r[1]); a.setAccountType(r[2]); accounts.save(a); }
            }
        };
    }
}
