let customers = [];
const BASE_URL = "http://localhost:8080/pahanaedu"

const getAllCustomers = async () => {
    try {
        const res = await fetch(`${BASE_URL}/customers`);
        if (!res.ok) throw new Error('Network response was not ok');
        const data = await res.json();
        customers = Array.isArray(data) ? data : (Array.isArray(data.data) ? data.data : []);
        renderTable();
        console.log("Customers", customers);

    } catch (err) {
        console.error('Failed to fetch customers:', err);
    }
}

getAllCustomers();

const renderTable = () => {
    const tableBody = document.getElementById("customerTableBody");
    tableBody.innerHTML = customers.map((c, i) => `
          <tr data-index="${i}">
            <td>${c.id ?? ''}</td>
            <td>${c.name ?? ''}</td>
            <td>${c.address ?? ''}</td>
            <td>${c.phone ?? ''}</td>
            <td>${c.email ?? ''}</td>
          </tr>
        `).join('');

    Array.from(tableBody.querySelectorAll('tr')).forEach(row => {
        row.addEventListener('click', function (e) {

            if (e.target.tagName === 'BUTTON') return;
            const idx = this.getAttribute('data-index');
            const c = customers[idx];
            document.getElementById('customerId').value = c.id ?? '';
            document.getElementById('customerName').value = c.name ?? '';
            document.getElementById('customerAddress').value = c.address ?? '';
            document.getElementById('customerPhone').value = c.phone ?? '';
            document.getElementById('customerEmail').value = c.email ?? '';
            document.getElementById('updateBtn').disabled = false;
            document.getElementById('deleteBtn').disabled = false;
        });
    });
}


// getAllCustomers()
renderTable()

//Create customer
const postCustomer = async (customer) => {
    try {
        const res = await fetch(`${BASE_URL}/customers`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });
        if (!res.ok) throw new Error('Failed to add customer');
        const data = await res.json();
        // refresh the customer list
        await getAllCustomers();
        //  clear the form after successful add
        document.getElementById('customerForm').reset();
        document.getElementById('updateBtn').disabled = true;
        return data;
    } catch (err) {
        alert('Error adding customer: ' + err.message);
        console.error(err);
    }
}

document.getElementById('customerForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    const customer = {
        // id is not sent for new customer (readonly)
        name: document.getElementById('customerName').value.trim(),
        address: document.getElementById('customerAddress').value.trim(),
        phone: document.getElementById('customerPhone').value.trim(),
        email: document.getElementById('customerEmail').value.trim()
    };
    await postCustomer(customer);
});

//update Customer
const updateCustomer = async () => {
    const id = document.getElementById('customerId').value.trim();
    if (!id) {
        alert('No customer selected for update.');
        return;
    }
    const customer = {
        id: id,
        name: document.getElementById('customerName').value.trim(),
        address: document.getElementById('customerAddress').value.trim(),
        phone: document.getElementById('customerPhone').value.trim(),
        email: document.getElementById('customerEmail').value.trim()
    };
    console.log(customer);
    try {
        const res = await fetch(`${BASE_URL}/customers?id=${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });
        if (!res.ok) throw new Error('Failed to update customer');
        await getAllCustomers();
        document.getElementById('customerForm').reset();
        document.getElementById('updateBtn').disabled = true;
    } catch (err) {
        alert('Error updating customer: ' + err.message);
        console.error(err);
    }
}

document.getElementById('updateBtn').addEventListener('click', async function () {
    await updateCustomer();
});

// Delete customer by id
const deleteCustomer = async (id) => {
    if (!id) {
        alert('No customer selected for delete.');
        return;
    }
    // Find customer for confirmation
    const customer = customers.find(c => c.id == id);
    if (!customer) {
        alert('Customer not found.');
        return;
    }
    if (!confirm(`Are you sure you want to delete customer ${customer.name}?`)) return;
    try {
        const res = await fetch(`${BASE_URL}/customers?id=${id}`, {
            method: 'DELETE',
        });
        if (!res.ok) throw new Error('Failed to delete customer');
        await getAllCustomers();
        document.getElementById('customerForm').reset();
        document.getElementById('updateBtn').disabled = true;
        document.getElementById('deleteBtn').disabled = true;
    } catch (err) {
        alert('Error deleting customer: ' + err.message);
        console.error(err);
    }
}

document.getElementById('deleteBtn').addEventListener('click', async function () {
    const id = document.getElementById('customerId').value.trim();
    await deleteCustomer(id);
});

// Clear form and disable buttons
function clearCustomerForm() {
    document.getElementById('customerForm').reset();
    document.getElementById('updateBtn').disabled = true;
    document.getElementById('deleteBtn').disabled = true;
}

document.getElementById('clearBtn').addEventListener('click', clearCustomerForm);

