const BASE_URL = "http://localhost:8080/pahanaedu";


function addInvoiceRowClickEvents() {
	const tbody = document.getElementById('invoiceTableBody');
	Array.from(tbody.querySelectorAll('tr')).forEach(row => {
		row.addEventListener('click', function () {
			const idCell = this.querySelector('td');
			if (idCell) {
				document.getElementById('invoiceIdInput').value = idCell.textContent.trim();
			}
		});
	});
}

window.addEventListener('DOMContentLoaded', async () => {
	const data = await getAllInvoices();
	const invoices = Array.isArray(data) ? data : (Array.isArray(data?.data) ? data.data : []);
	renderInvoiceTable(invoices);
	addInvoiceRowClickEvents();
});



async function getAllInvoices() {
	try {
		const res = await fetch(`${BASE_URL}/invoices`);
		if (!res.ok) throw new Error('Failed to fetch invoices');
		const data = await res.json();
		console.log('All invoices:', data);
		return data;
	} catch (err) {
		console.error('Error fetching invoices:', err);
		return null;
	}
}

function renderInvoiceTable(invoices) {
	const tbody = document.getElementById('invoiceTableBody');
	if (!Array.isArray(invoices)) {
		tbody.innerHTML = '<tr><td colspan="11" class="text-center">No data found</td></tr>';
		return;
	}
	tbody.innerHTML = invoices.map(inv => `
		<tr>
			<td>${inv.id ?? ''}</td>
			<td>${inv.invoiceNo ?? ''}</td>
			<td>${inv.customerId ?? ''}</td>
			<td>${inv.invoiceDate ? inv.invoiceDate.split('T')[0] : ''}</td>
			<td>${inv.subTotal ?? ''}</td>
			<td>${inv.tax ?? inv.taxAmount ?? ''}</td>
			<td>${inv.discountAmt ?? inv.discount ?? ''}</td>
			<td>${inv.total ?? inv.totalAmount ?? ''}</td>
			<td>${inv.status ?? ''}</td>
			<td>${inv.createdBy ?? ''}</td>
			
		</tr>
	`).join('');
}

document.getElementById('printBillBtn').addEventListener('click', function () {
    //add here
    const invoiceId = document.getElementById('invoiceIdInput').value.trim();
    console.log("Invoice ID",invoiceId);
    
    if (invoiceId) {
        window.open(`invoice.html?invoiceId=${encodeURIComponent(invoiceId)}`, '_blank');
    } else {
        alert('Please select or enter an Invoice ID.');
    }
});



getAllInvoices()