# Tele-Veterinary Clinical Diagnostics & Remote Livestock Healthcare System

A Spring Boot + MySQL application for remote livestock consultations, veterinary diagnostic workflows, invoicing, vendor bills, payments, budgets, double-entry journal records, and management reports.

## Technology
- Java 17
- Spring Boot 3.5.5
- Spring Web
- Spring Data JPA / Hibernate
- MySQL
- Maven
- HTML, CSS, JavaScript frontend served by Spring Boot
- Postman for API testing

## MySQL
The project is configured with:
- Host: localhost
- Port: 3306
- Database: tele_veterinary
- Username: root
- Password: root

If your MySQL password is different, edit `src/main/resources/application.properties`.

The database is automatically created if it does not exist, and JPA creates/updates the tables.

## Run in IntelliJ IDEA
1. Open this project folder in IntelliJ IDEA.
2. Make sure JDK 17 is selected.
3. Make sure MySQL Server is running.
4. Reload Maven dependencies.
5. Run `TeleVeterinaryApplication.java`.
6. Open `http://localhost:8080`.

The first run creates sample farmer, specialist, lab vendor, products, and chart-of-account records.

## Main frontend sections
- Dashboard
- Master Data
- Consultations
- Finance
- Budget
- Reports

## Important API endpoints

### Contacts
POST `/api/contacts`
```json
{
  "name":"Ravi Kumar",
  "contactType":"FARMER",
  "phone":"9876543210",
  "email":"ravi@example.com",
  "address":"Pollachi",
  "region":"Regional Tele-Vet Hub 2"
}
```

GET `/api/contacts`

### Products
POST `/api/products`
```json
{
  "name":"Veterinary Antibiotic Kit",
  "productType":"GOODS",
  "category":"MEDICINE",
  "price":850,
  "stockQuantity":25,
  "description":"Veterinary medicine kit"
}
```

GET `/api/products`

### Chart of Accounts
POST `/api/accounts`
```json
{
  "code":"5020",
  "name":"Diagnostic Testing Costs",
  "accountType":"EXPENSE",
  "balance":0
}
```

### Tele-consultation
POST `/api/televet/consultations`
```json
{
  "farmer":{"id":1},
  "specialist":{"id":2},
  "scheduledAt":"2026-09-28T10:30:00",
  "livestockType":"Cow",
  "symptoms":"High fever and swelling",
  "diagnosis":"Clinical infection",
  "prescription":"Follow veterinary prescription",
  "diagnosticTests":"Blood test",
  "status":"SCHEDULED"
}
```

The backend calculates diagnostic priority from symptom text when priority is not supplied.

### Customer Invoice
POST `/api/invoices`
```json
{
  "customer":{"id":1},
  "consultation":{"id":1},
  "consultationAmount":500,
  "medicineAmount":850
}
```

### Vendor Bill
POST `/api/vendor-bills`
```json
{
  "vendor":{"id":3},
  "purchaseOrderReference":"PO-1001",
  "description":"Diagnostic test kits",
  "amount":3000
}
```

### Payment
Customer receipt:
POST `/api/payments`
```json
{
  "paymentType":"CUSTOMER_RECEIPT",
  "method":"UPI",
  "amount":1350,
  "referenceNumber":"UPI-1001",
  "invoiceId":1
}
```

Vendor payment:
POST `/api/payments`
```json
{
  "paymentType":"VENDOR_PAYMENT",
  "method":"BANK",
  "amount":3000,
  "referenceNumber":"BANK-1001",
  "vendorBillId":1
}
```

### Budget
POST `/api/budgets`
```json
{
  "analyticAccount":"Regional Tele-Vet Hub 2",
  "financialYear":"2026-27",
  "season":"Monsoon",
  "allocatedAmount":100000,
  "actualAmount":25000,
  "region":"Pollachi"
}
```

### Reports
GET `/api/dashboard`
GET `/api/reports`
GET `/api/journals`

## Transaction and accounting logic
- Customer invoices create sales journal entries.
- Vendor bills create purchase journal entries.
- Customer receipts create cash/bank entries against customer receivables.
- Vendor payments reduce pharma lab payables.
- Dashboard and reports calculate revenue, expenses, cash flow, payables, P&L, and budget utilization.

## Postman
Base URL:
`http://localhost:8080`

Recommended order:
1. GET `/api/contacts`
2. POST `/api/televet/consultations`
3. POST `/api/invoices`
4. POST `/api/vendor-bills`
5. POST `/api/payments`
6. POST `/api/budgets`
7. GET `/api/reports`
8. GET `/api/journals`

## Notes
This is a local academic/project implementation. Clinical prescriptions and diagnostic decisions should be entered/reviewed by qualified veterinary professionals.
