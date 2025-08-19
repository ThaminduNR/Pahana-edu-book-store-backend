let customers = [];
const BASE_URL = "http://localhost:8080/pahanaedu"
const status = "ISSUED"
const createdBy = 1;
const taxRate = 0;
let discountAmt = 0
document.getElementById('itemQty').value = 0;

const getAllCustomers = async () => {
  try {
    const res = await fetch(`${BASE_URL}/customers`);
    if (!res.ok) throw new Error('Network response was not ok');
    const data = await res.json();
    customers = Array.isArray(data) ? data : (Array.isArray(data.data) ? data.data : []);
    console.log("Customers place order form", customers);

  } catch (err) {
    console.error('Failed to fetch customers:', err);
  }
}

const loadCustomerIdDropdown = () => {
  const customerIdSelect = document.getElementById('customerId');
  customerIdSelect.innerHTML = customers.map(c => `<option value="${c.id}">${c.id}</option>`).join('');
};

const setCustomerNameField = () => {
  const customerIdSelect = document.getElementById('customerId');
  const customerNameInput = document.getElementById('customerName');

  console.log(customerIdSelect)


  customerIdSelect.addEventListener('change', function() {
    const selectedId = this.value;
    const customer = customers.find(c => c.id == selectedId);
    customerNameInput.value = customer ? customer.name : '';
  });
};

getAllCustomers().then(() => {
  loadCustomerIdDropdown();
  setCustomerNameField();
});


let allItems = [];
let items = [];

async function getAllItems() {
  try {
    const res = await fetch(`${BASE_URL}/items`);
    if (!res.ok) throw new Error('Network response was not ok');
    const data = await res.json();
    allItems = Array.isArray(data) ? data : (Array.isArray(data.data) ? data.data : []);
    console.log("Items fetched successfully:", allItems);

    console.log("All Items", allItems);
  } catch (err) {
    console.error('Failed to fetch items:', err);
  }
}

const loadItemDropdown = () => {
  const itemNameSelect = document.getElementById('itemName');
  itemNameSelect.innerHTML = allItems.map(item => `<option value="${item.id}">${item.name}</option>`).join('');
};

function calculateTotalAmount() {
  return items.reduce((sum, item) => sum + (item.unitPrice * item.qty), 0);
}















document.getElementById('addToCartBtn').addEventListener('click', function () {
  
  const itemId = document.getElementById('itemName').value;
  const qty = parseInt(document.getElementById('itemQty').value);
  const item = allItems.find(i => i.id == itemId);
  console.log("Item Id", itemId)
  console.log("QTY",qty)
  console.log("Item",item)
 
  if (!item || qty < 1) {
    alert('Please select a valid item and quantity.');
    return;
  }
  // Check if item already in cart
  const cartItem = items.find(ci => ci.id == itemId);
  console.log("Cart item table", cartItem);
  if (cartItem) {
    cartItem.qty += qty;
  } else {
    items.push({
      id: item.id,
      name: item.name,
      unitPrice: item.unitPrice,
      qty: qty
    });
  }
  renderCartTable();
});

function renderCartTable() {
  //console.log("Cart items", items);
  const qty = parseInt(document.getElementById('itemQty').value);

  const tbody = document.getElementById('cartTableBody');
  tbody.innerHTML = items.map((ci, idx) => `
    <tr>
      <td>${ci.name}</td>
      <td>${ci.qty}</td>
      <td>${ci.unitPrice}</td>
      <td>${(ci.unitPrice * ci.qty).toFixed(2)}</td>
      <td><button class='btn btn-sm btn-danger' onclick='removeCartItem(${idx})'>Remove</button></td>
    </tr>
  `).join('');

  document.getElementById('totalAmt').textContent = calculateTotalAmount().toFixed(2);

}

window.removeCartItem = function(idx) {
  items.splice(idx, 1);
  renderCartTable();
}



getAllItems().then(loadItemDropdown);


function getDiscountAmount() {
  const discountInput = document.getElementById('discountAmt');
  discountAmt = parseFloat(discountInput.value) || 0;
  console.log("Discount Amount", discountAmt);
}

// Place Order button 
document.getElementById('placeOrderBtn').addEventListener('click', async function () {
  if (items.length === 0) {
    alert('Please add at least one item to the cart before placing the order.');
    return;
  }
  const customerId = parseInt(document.getElementById('customerId').value);
  getDiscountAmount();
  const payloadItems = items.map(i => ({
    itemId: i.id,
    quantity: i.qty,
    unitPrice: i.unitPrice
  }));

  const payload = {
    invoice: {
      customerId: customerId,
      discountAmt: discountAmt,
      status: status,
      createdBy: createdBy
    },
    taxRate: taxRate,
    items: payloadItems
  };

  console.log("Payload", payload);
  try {
    const res = await fetch(`${BASE_URL}/invoices`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error('Failed to place order');
    const data = await res.json();
    alert('Order placed successfully!');
    items = [];
    renderCartTable();
    document.getElementById('discountAmt').value = '';
    document.getElementById('itemQty').value = 0;
  } catch (err) {
    alert('Error placing order: ' + err.message);
  }
});
