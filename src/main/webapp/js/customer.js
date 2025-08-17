let customers = [];
const BASE_URL = "http://localhost:8080/pahanaedu"

async function getAllCustomers() {
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

function renderTable() {
    const tableBody = document.getElementById("customerTableBody");
    tableBody.innerHTML = customers.map((c, i) => `
          <tr>
            <td>${c.id ?? ''}</td>
            <td>${c.name ?? ''}</td>
            <td>${c.address ?? ''}</td>
            <td>${c.phone ?? ''}</td>
            <td>${c.email ?? ''}</td>
            <td>
              <button class='btn btn-sm btn-danger' onclick='deleteCustomer(${i})'>Delete</button>
            </td>
          </tr>
        `).join('');
}
// getAllCustomers()
renderTable()