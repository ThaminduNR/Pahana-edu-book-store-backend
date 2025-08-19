
const BASE_URL = "http://localhost:8080/pahanaedu"

let items = [];

async function getAllItems() {
    try {
        const res = await fetch(`${BASE_URL}/items`);
        if (!res.ok) throw new Error('Network response was not ok');
        const data = await res.json();
        items = Array.isArray(data) ? data : (Array.isArray(data.data) ? data.data : []);
        console.log("Items fetched successfully:", items);
        renderItemTable();
        console.log("Items", items);
    } catch (err) {
        console.error('Failed to fetch items:', err);
    }
}

function renderItemTable() {
    const tableBody = document.getElementById("itemTableBody");
    tableBody.innerHTML = items.map((item, i) => `
        <tr data-index="${i}">
            <td>${item.id ?? ''}</td>
            <td>${item.code ?? ''}</td>
            <td>${item.name ?? ''}</td>
            <td>${item.description ?? ''}</td>
            <td>${item.unitPrice ?? ''}</td>
            <td>${item.qty ?? item.quantity ?? ''}</td>
        </tr>
    `).join('');
    // Add row click event to set data to form
    Array.from(tableBody.querySelectorAll('tr')).forEach(row => {
        row.addEventListener('click', function () {
            const idx = this.getAttribute('data-index');
            const item = items[idx];
            document.getElementById('itemId').value = item.id ?? '';
            document.getElementById('itemCode').value = item.code ?? '';
            document.getElementById('itemName').value = item.name ?? '';
            document.getElementById('itemDescription').value = item.description ?? '';
            document.getElementById('itemUnitPrice').value = item.unitPrice ?? '';
            document.getElementById('itemQuantity').value = item.qty ?? item.quantity ?? '';
            document.getElementById('updateBtn').disabled = false;
            document.getElementById('deleteBtn').disabled = false;
        });
    });
}

async function postItem(item) {
    try {
        const res = await fetch(`${BASE_URL}/items`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item)
        });
        const data = await res.json();
        if (!res.ok) {
            swal(data.message || 'Failed to create item');
            return;
        }
        swal("Book Added Succesfully")
        getAllItems();
    } catch (err) {
        console.error('Failed to create item:', err.message);
        swal('Failed to create item: ' + err.message);
    }
}


async function updateItem(item) {
    try {
        const res = await fetch(`${BASE_URL}/items?id=${item.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item)
        });
        if (!res.ok) throw new Error('Network response was not ok');
        const data = await res.json();
        console.log("Item updated successfully:", data);
        swal("Book Update Succesfully")
        getAllItems();
    } catch (err) {
        console.error('Failed to update item:', err);
        swal('Failed to update item: ' + err.message);
    }
}


async function deleteItem(itemId) {
    
    const willDelete = await new Promise(resolve => {
        swal({
            title: "Are you sure?",
            text: "Do you want to delete this item?",
            icon: "warning",
            buttons: ["Cancel", "Delete"],
            dangerMode: true,
        }).then(willDelete => resolve(willDelete));
    });
    if (!willDelete) return;
    try {
        const res = await fetch(`${BASE_URL}/items?id=${itemId}`, {
            method: 'DELETE'
        });
        if (!res.ok) throw new Error('Network response was not ok');
        swal("Book Delete Succesfully");
        getAllItems();
    } catch (err) {
        console.error('Failed to delete item:', err);
        swal('Failed to delete item: ' + err.message);
    }
}

document.getElementById('itemForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const item = {
        code: document.getElementById('itemCode').value.trim(),
        name: document.getElementById('itemName').value.trim(),
        description: document.getElementById('itemDescription').value.trim(),
        unitPrice: parseFloat(document.getElementById('itemUnitPrice').value),
        qty: parseInt(document.getElementById('itemQuantity').value)
    };
    postItem(item);
});

document.getElementById('updateBtn').addEventListener('click', function() {
    const itemId = document.getElementById('itemId').value.trim();
    if (!itemId) {
        swal('No item selected for update.');
        return;
    }
    const item = {
        id: itemId,
        code: document.getElementById('itemCode').value.trim(),
        name: document.getElementById('itemName').value.trim(),
        description: document.getElementById('itemDescription').value.trim(),
        unitPrice: parseFloat(document.getElementById('itemUnitPrice').value),
        qty: parseInt(document.getElementById('itemQuantity').value)
    };

    console.log("Updating item:", item);
    updateItem(item);
});
document.getElementById('deleteBtn').addEventListener('click', function() {
    const itemId = document.getElementById('itemId').value.trim();
    if (!itemId) {
        swal('No item selected for delete.');
        return;
    }
    deleteItem(itemId);
});

getAllItems();

document.getElementById('clearBtn').addEventListener('click', function() {
    document.getElementById('itemForm').reset();
    document.getElementById('itemId').value = '';
    document.getElementById('updateBtn').disabled = true;
    document.getElementById('deleteBtn').disabled = true;
});