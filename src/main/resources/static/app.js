const api = '/api';
const content = document.getElementById('content');
const title = document.getElementById('pageTitle');

async function get(url){ const r=await fetch(api+url); if(!r.ok) throw new Error(await r.text()); return r.json(); }
async function post(url,data){ const r=await fetch(api+url,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(data)}); if(!r.ok) throw new Error(await r.text()); return r.json(); }
async function put(url,data){ const r=await fetch(api+url,{method:'PUT',headers:{'Content-Type':'application/json'},body:JSON.stringify(data)}); if(!r.ok) throw new Error(await r.text()); return r.json(); }

document.querySelectorAll('.nav').forEach(b=>b.onclick=()=>loadPage(b.dataset.page));

function esc(v){return v==null?'':String(v).replace(/[&<>"']/g,m=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[m]));}
function money(v){return '₹'+Number(v||0).toLocaleString('en-IN',{minimumFractionDigits:2});}
function table(headers,rows){return `<table><thead><tr>${headers.map(h=>`<th>${h}</th>`).join('')}</tr></thead><tbody>${rows.length?rows.join(''):`<tr><td colspan="${headers.length}" class="empty">No records yet</td></tr>`}</tbody></table>`}

async function loadPage(page){
 document.querySelectorAll('.nav').forEach(b=>b.classList.toggle('active',b.dataset.page===page));
 title.textContent=page==='dashboard'?'Dashboard':page==='masters'?'Master Data':page[0].toUpperCase()+page.slice(1);
 content.innerHTML='<div class="panel">Loading...</div>';
 try{
   if(page==='dashboard') await dashboard();
   if(page==='masters') await masters();
   if(page==='consultations') await consultations();
   if(page==='finance') await finance();
   if(page==='budget') await budget();
   if(page==='reports') await reports();
 }catch(e){content.innerHTML=`<div class="panel"><b>Could not load data.</b><p>${esc(e.message)}</p><p>Make sure MySQL is running and the Spring Boot application is started.</p></div>`}
}

async function dashboard(){
 const d=await get('/dashboard');
 content.innerHTML=`
 <div class="cards">
  <div class="card"><div class="label">Farmers</div><div class="value">${d.farmers}</div></div>
  <div class="card"><div class="label">Consultations</div><div class="value">${d.consultations}</div></div>
  <div class="card"><div class="label">Revenue</div><div class="value">${money(d.revenue)}</div></div>
  <div class="card"><div class="label">Net Result</div><div class="value">${money(d.netResult)}</div></div>
 </div>
 <div class="grid2">
  <div class="panel"><h3>Clinical Activity</h3>
   <div class="metric"><span>Veterinary specialists</span><b>${d.specialists}</b></div>
   <div class="metric"><span>Customer invoices</span><b>${d.invoices}</b></div>
   <div class="metric"><span>Vendor bills</span><b>${d.vendorBills}</b></div>
  </div>
  <div class="panel"><h3>Financial Snapshot</h3>
   <div class="metric"><span>Cash received</span><b>${money(d.cashReceived)}</b></div>
   <div class="metric"><span>Vendor payments</span><b>${money(d.vendorPayments)}</b></div>
   <div class="metric"><span>Lab expenses</span><b>${money(d.labExpenses)}</b></div>
  </div>
 </div>
 <div class="panel" style="margin-top:18px"><h3>Workflow</h3><div class="notice">Farmer → Tele-consultation → Diagnosis / Prescription → Customer Invoice → Payment. Lab Purchase → Vendor Bill → Bank Payment → Ledger.</div></div>`;
}

async function masters(){
 const [cs,ps,as]=await Promise.all([get('/contacts'),get('/products'),get('/accounts')]);
 content.innerHTML=`
 <div class="grid2">
  <div class="panel"><h3>Contact Master</h3><div class="toolbar"><button class="btn" onclick="contactForm()">+ Add Contact</button></div>
   ${table(['Name','Type','Phone','Region'],cs.map(c=>`<tr><td>${esc(c.name)}</td><td><span class="pill">${esc(c.contactType)}</span></td><td>${esc(c.phone)}</td><td>${esc(c.region)}</td></tr>`))}
  </div>
  <div class="panel"><h3>Product Master</h3><div class="toolbar"><button class="btn" onclick="productForm()">+ Add Product</button></div>
   ${table(['Product','Type','Price','Stock'],ps.map(p=>`<tr><td>${esc(p.name)}</td><td>${esc(p.productType)}</td><td>${money(p.price)}</td><td>${p.stockQuantity}</td></tr>`))}
  </div>
 </div>
 <div class="panel" style="margin-top:18px"><h3>Chart of Accounts</h3>
 ${table(['Code','Account','Type','Balance'],as.map(a=>`<tr><td>${esc(a.code)}</td><td>${esc(a.name)}</td><td>${esc(a.accountType)}</td><td>${money(a.balance)}</td></tr>`))}</div>`;
}

function modal(html){
 let m=document.createElement('div');m.className='modal open';m.innerHTML=`<div class="modalbox"><button class="close" onclick="this.closest('.modal').remove()">×</button>${html}</div>`;document.body.appendChild(m);
}
async function contactForm(){
 modal(`<h3>Add Contact</h3><form id="f" class="form">
 <div><label>Name</label><input name="name" required></div><div><label>Type</label><select name="contactType"><option>FARMER</option><option>SPECIALIST</option><option>LAB_VENDOR</option></select></div>
 <div><label>Phone</label><input name="phone"></div><div><label>Email</label><input name="email"></div><div class="full"><label>Address</label><input name="address"></div><div><label>Region</label><input name="region"></div>
 <div class="full"><button class="btn">Save Contact</button></div></form>`);
 document.getElementById('f').onsubmit=async e=>{e.preventDefault();let o=Object.fromEntries(new FormData(e.target));await post('/contacts',o);e.target.closest('.modal').remove();masters();}
}
async function productForm(){
 modal(`<h3>Add Product</h3><form id="f" class="form">
 <div><label>Name</label><input name="name" required></div><div><label>Type</label><select name="productType"><option>SERVICE</option><option>GOODS</option></select></div>
 <div><label>Category</label><input name="category"></div><div><label>Price</label><input name="price" type="number" step="0.01"></div><div><label>Stock</label><input name="stockQuantity" type="number"></div><div class="full"><label>Description</label><textarea name="description"></textarea></div>
 <div class="full"><button class="btn">Save Product</button></div></form>`);
 document.getElementById('f').onsubmit=async e=>{e.preventDefault();let o=Object.fromEntries(new FormData(e.target));o.price=Number(o.price||0);o.stockQuantity=Number(o.stockQuantity||0);await post('/products',o);e.target.closest('.modal').remove();masters();}
}

async function consultations(){
 const [cs,con]=await Promise.all([get('/contacts'),get('/televet/consultations')]);
 const farmers=cs.filter(c=>c.contactType==='FARMER'), docs=cs.filter(c=>c.contactType==='SPECIALIST');
 content.innerHTML=`<div class="panel"><div class="toolbar"><button class="btn" onclick='consultForm(${JSON.stringify(farmers)},${JSON.stringify(docs)})'>+ Schedule Consultation</button></div>
 ${table(['Farmer','Livestock','Schedule','Priority','Status','Prescription'],con.map(c=>`<tr><td>${esc(c.farmer?.name)}</td><td>${esc(c.livestockType)}</td><td>${esc(c.scheduledAt||'')}</td><td><span class="pill ${String(c.priority).toLowerCase()}">${esc(c.priority)}</span></td><td>${esc(c.status)}</td><td>${esc(c.prescription||'—')}</td></tr>`))}</div>`;
}
function consultForm(farmers,docs){
 modal(`<h3>Schedule Tele-Consultation</h3><form id="f" class="form">
 <div><label>Farmer</label><select name="farmerId">${farmers.map(x=>`<option value="${x.id}">${esc(x.name)}</option>`).join('')}</select></div>
 <div><label>Veterinary Specialist</label><select name="specialistId">${docs.map(x=>`<option value="${x.id}">${esc(x.name)}</option>`).join('')}</select></div>
 <div><label>Scheduled At</label><input name="scheduledAt" type="datetime-local" required></div><div><label>Livestock Type</label><input name="livestockType" placeholder="Cow / Goat / Buffalo"></div>
 <div class="full"><label>Symptoms</label><textarea name="symptoms" placeholder="Example: high fever, swelling, not eating"></textarea></div>
 <div class="full"><label>Prescription</label><textarea name="prescription"></textarea></div><div class="full"><label>Diagnostic Tests</label><input name="diagnosticTests" placeholder="Blood test, milk test, etc."></div>
 <div class="full"><button class="btn">Schedule & Calculate Priority</button></div></form>`);
 document.getElementById('f').onsubmit=async e=>{e.preventDefault();let x=Object.fromEntries(new FormData(e.target));let o={...x,farmer:{id:Number(x.farmerId)},specialist:{id:Number(x.specialistId)}};delete o.farmerId;delete o.specialistId;await post('/televet/consultations',o);e.target.closest('.modal').remove();consultations();}
}

async function finance(){
 const [cs,ins,bills,pays]=await Promise.all([get('/contacts'),get('/invoices'),get('/vendor-bills'),get('/payments')]);
 content.innerHTML=`
 <div class="grid2">
  <div class="panel"><h3>Customer Invoices</h3><div class="toolbar"><button class="btn" onclick='invoiceForm(${JSON.stringify(cs.filter(x=>x.contactType==="FARMER"))})'>+ Customer Invoice</button></div>
  ${table(['Invoice','Customer','Consultation','Medicine','Total','Status'],ins.map(i=>`<tr><td>${esc(i.invoiceNumber)}</td><td>${esc(i.customer?.name)}</td><td>${money(i.consultationAmount)}</td><td>${money(i.medicineAmount)}</td><td>${money(i.totalAmount)}</td><td>${esc(i.paymentStatus)}</td></tr>`))}</div>
  <div class="panel"><h3>Vendor Bills</h3><div class="toolbar"><button class="btn" onclick='billForm(${JSON.stringify(cs.filter(x=>x.contactType==="LAB_VENDOR"))})'>+ Vendor Bill</button></div>
  ${table(['Bill','Vendor','Description','Amount','Status'],bills.map(b=>`<tr><td>${esc(b.billNumber)}</td><td>${esc(b.vendor?.name)}</td><td>${esc(b.description)}</td><td>${money(b.amount)}</td><td>${esc(b.paymentStatus)}</td></tr>`))}</div>
 </div>
 <div class="panel" style="margin-top:18px"><h3>Payments</h3><div class="toolbar"><button class="btn" onclick='paymentForm(${JSON.stringify(ins)},${JSON.stringify(bills)})'>+ Register Payment</button></div>
 ${table(['Type','Method','Amount','Reference','Date'],pays.map(p=>`<tr><td>${esc(p.paymentType)}</td><td>${esc(p.method)}</td><td>${money(p.amount)}</td><td>${esc(p.referenceNumber)}</td><td>${esc(p.paidAt||'')}</td></tr>`))}</div>`;
}
function invoiceForm(farmers){
 modal(`<h3>Create Customer Invoice</h3><form id="f" class="form">
 <div><label>Farmer</label><select name="customerId">${farmers.map(x=>`<option value="${x.id}">${esc(x.name)}</option>`).join('')}</select></div>
 <div><label>Consultation ID (optional)</label><input name="consultationId" type="number"></div>
 <div><label>Consultation Amount</label><input name="consultationAmount" type="number" step=".01"></div><div><label>Medicine Amount</label><input name="medicineAmount" type="number" step=".01"></div>
 <div class="full"><button class="btn">Create Invoice</button></div></form>`);
 document.getElementById('f').onsubmit=async e=>{e.preventDefault();let x=Object.fromEntries(new FormData(e.target));let o={customer:{id:Number(x.customerId)},consultationAmount:Number(x.consultationAmount||0),medicineAmount:Number(x.medicineAmount||0)};if(x.consultationId)o.consultation={id:Number(x.consultationId)};await post('/invoices',o);e.target.closest('.modal').remove();finance();}
}
function billForm(vendors){
 modal(`<h3>Create Vendor Bill</h3><form id="f" class="form">
 <div><label>Lab Vendor</label><select name="vendorId">${vendors.map(x=>`<option value="${x.id}">${esc(x.name)}</option>`).join('')}</select></div>
 <div><label>Purchase Order Reference</label><input name="purchaseOrderReference" placeholder="PO-1001"></div><div class="full"><label>Description</label><input name="description" placeholder="Diagnostic test kits"></div><div><label>Amount</label><input name="amount" type="number" step=".01"></div>
 <div class="full"><button class="btn">Create Vendor Bill</button></div></form>`);
 document.getElementById('f').onsubmit=async e=>{e.preventDefault();let x=Object.fromEntries(new FormData(e.target));x.vendor={id:Number(x.vendorId)};delete x.vendorId;x.amount=Number(x.amount||0);await post('/vendor-bills',x);e.target.closest('.modal').remove();finance();}
}
function paymentForm(ins,bills){
 modal(`<h3>Register Payment</h3><form id="f" class="form">
 <div><label>Payment Type</label><select name="paymentType"><option>CUSTOMER_RECEIPT</option><option>VENDOR_PAYMENT</option></select></div><div><label>Method</label><select name="method"><option>UPI</option><option>BANK</option><option>CASH</option><option>CARD</option></select></div>
 <div><label>Invoice ID</label><input name="invoiceId" type="number" placeholder="For customer receipt"></div><div><label>Vendor Bill ID</label><input name="vendorBillId" type="number" placeholder="For vendor payment"></div>
 <div><label>Amount</label><input name="amount" type="number" step=".01"></div><div><label>Reference</label><input name="referenceNumber"></div>
 <div class="full"><button class="btn">Register Payment</button></div></form>`);
 document.getElementById('f').onsubmit=async e=>{e.preventDefault();let x=Object.fromEntries(new FormData(e.target));x.amount=Number(x.amount||0);if(x.invoiceId)x.invoiceId=Number(x.invoiceId);else delete x.invoiceId;if(x.vendorBillId)x.vendorBillId=Number(x.vendorBillId);else delete x.vendorBillId;await post('/payments',x);e.target.closest('.modal').remove();finance();}
}

async function budget(){
 const bs=await get('/budgets');
 content.innerHTML=`<div class="panel"><div class="toolbar"><button class="btn" onclick="budgetForm()">+ Add Regional Budget</button></div>
 ${table(['Analytic Account','Region','Year','Season','Allocated','Actual','Utilization'],bs.map(b=>`<tr><td>${esc(b.analyticAccount)}</td><td>${esc(b.region)}</td><td>${esc(b.financialYear)}</td><td>${esc(b.season)}</td><td>${money(b.allocatedAmount)}</td><td>${money(b.actualAmount)}</td><td>${b.utilization}%</td></tr>`))}</div>`;
}
function budgetForm(){
 modal(`<h3>Create Regional Healthcare Budget</h3><form id="f" class="form">
 <div><label>Analytic Account</label><input name="analyticAccount" value="Regional Tele-Vet Hub 2" required></div><div><label>Region</label><input name="region" placeholder="Pollachi"></div>
 <div><label>Financial Year</label><input name="financialYear" value="2026-27"></div><div><label>Season</label><input name="season" placeholder="Monsoon"></div>
 <div><label>Allocated Amount</label><input name="allocatedAmount" type="number" step=".01"></div><div><label>Actual Amount</label><input name="actualAmount" type="number" step=".01"></div>
 <div class="full"><button class="btn">Save Budget</button></div></form>`);
 document.getElementById('f').onsubmit=async e=>{e.preventDefault();let x=Object.fromEntries(new FormData(e.target));x.allocatedAmount=Number(x.allocatedAmount||0);x.actualAmount=Number(x.actualAmount||0);await post('/budgets',x);e.target.closest('.modal').remove();budget();}
}

async function reports(){
 const r=await get('/reports');
 content.innerHTML=`
 <div class="grid2">
  <div class="panel"><h3>Profit & Loss Account</h3>
   <div class="metric"><span>Tele-Consultation Revenue</span><b>${money(r.profitAndLoss.consultationRevenue)}</b></div>
   <div class="metric"><span>Pharmacy Sales</span><b>${money(r.profitAndLoss.pharmacyRevenue)}</b></div>
   <div class="metric"><span>Total Revenue</span><b>${money(r.profitAndLoss.totalRevenue)}</b></div>
   <div class="metric"><span>Lab Processing Expenses</span><b>${money(r.profitAndLoss.labProcessingExpenses)}</b></div>
   <div class="metric"><span>Net Result</span><b>${money(r.profitAndLoss.netResult)}</b></div>
  </div>
  <div class="panel"><h3>Balance Sheet Snapshot</h3>
   <div class="metric"><span>Cash / Bank Net</span><b>${money(r.balanceSheet.cashOrBankNet)}</b></div>
   <div class="metric"><span>Pharma Lab Payables</span><b>${money(r.balanceSheet.pharmaLabPayables)}</b></div>
  </div>
 </div>
 <div class="panel" style="margin-top:18px"><h3>Regional Budget Utilization</h3>
   <div class="metric"><span>Allocated</span><b>${money(r.budget.allocated)}</b></div>
   <div class="metric"><span>Actual</span><b>${money(r.budget.actual)}</b></div>
   <div class="metric"><span>Utilization</span><b>${r.budget.utilizationPercent}%</b></div>
 </div>`;
}

loadPage('dashboard');
