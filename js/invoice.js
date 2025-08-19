


const BASE_URL = "http://localhost:8080/pahanaedu"
let invoiceID = 0;

const invoiceNumber = 0;
let date = "0000 00 00";
const subTotal = 0;
const discount = 0;
const total = 0;

// Do not set .value on a <p> element. Use .textContent and set after data is loaded.


// getLastInvoiceID();

// async function getLastInvoiceID() {
//     try {
//     const res = await fetch(`${BASE_URL}/util`);
//     if (!res.ok) throw new Error('Failed to fetch last invoice ID');
//     const data = await res.json();
//     const lastInvoiceId = data.id || data.lastId || data;
//     invoiceID = lastInvoiceId.data;
//     console.log('Last Invoice ID:', lastInvoiceId);
//         if (lastInvoiceId.success == true) {
//             getBillDetail(invoiceID);
//         }
        
//   } catch (err) {
//     console.error('Error fetching last invoice ID:', err);
//     return null;
//   }
// }

const urlParams = new URLSearchParams(window.location.search);
const invoiceId = urlParams.get('invoiceId');
console.log("InvoiceID", invoiceId);

if (invoiceId) {
  getBillDetail(invoiceId);
}


async function getBillDetail(invoiceId) {
  try {
    const res = await fetch(`${BASE_URL}/bill?id=${invoiceId}`);
    if (!res.ok) throw new Error('Failed to fetch bill details');
    const result = await res.json();
    // Set invoice number and date in the HTML if data exists
    if (result && result.data) {
      const data = result.data;
      if (document.getElementById('invoiceNumber')) {
        document.getElementById('invoiceNumber').textContent = 'Invoice Number - ' + (data.invoiceNumber || '');
      }
      if (document.getElementById('invoiceDate')) {
        // Format date as yyyy-mm-dd if needed
          let formattedDate = '';
          console.log(data);
          
        if (data.invoiceDate) {
          const d = new Date(data.invoiceDate);
          formattedDate = data.invoiceDate.split('T')[0];
        }
        document.getElementById('invoiceDate').textContent = 'Invoice Date - ' + formattedDate;
      }
      // Set subtotal, discount, and total
      if (document.getElementById('subTotal')) {
        document.getElementById('subTotal').textContent = 'Sub Total = ' + Number(data.subTotal || 0).toFixed(2);
      }
      if (document.getElementById('discount')) {
        document.getElementById('discount').textContent = 'Discount = ' + Number(data.discount || 0).toFixed(2);
      }
      if (document.getElementById('total')) {
        document.getElementById('total').innerHTML = '<strong>Total = ' + Number(data.total || 0).toFixed(2) + '</strong>';
      }
      // Create invoiceItemsTable with header and rows
      const table = document.getElementById('invoiceItemsTable');
      if (table && Array.isArray(data.items)) {
        table.innerHTML = '';
        // Add header row
        const header = document.createElement('tr');
  header.innerHTML = '<th style="text-align:left">Item Name</th><th style="text-align:center">Quantity</th><th style="text-align:right">Total</th>';
        table.appendChild(header);
        // Add item rows
        data.items.forEach(item => {
          const row = document.createElement('tr');
          row.innerHTML = `
            <td style="text-align:left">${item.itemName}</td>
            <td style="text-align:center">${item.quantity}</td>
            <td style="text-align:right">${Number(item.lineTotal || (item.unitPrice * item.quantity)).toFixed(2)}</td>
          `;
          table.appendChild(row);
        });
      }
    }
    return result;
  } catch (err) {
    console.error('Error fetching bill details:', err);
    return null;
  }
}
