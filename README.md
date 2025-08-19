# Pahana-Edu Book Store – Frontend

Frontend for the Pahana-Edu Book Store and Invoicing System.

## 📦 Features

- Dashboard with summary cards (Customers, Items, Sales, Orders, Revenue)
- Customer management (CRUD)
- Item management (CRUD)
- Place Order (create invoice, add items, discounts)
- Bill/Invoice print and list
- Responsive UI with Bootstrap 5

## � Getting Started

1. Clone this repo and open the `frontend` folder in VS Code or your editor.
2. Open `index.html` or `login.html` in your browser to start.
3. Make sure the backend (Java EE, Tomcat, MySQL) is running and accessible at the configured BASE_URL in JS files.

## 🛠️ Project Structure

- `index.html` – Login page
- `dashboard.html` – Main dashboard with navigation
- `home.html` – Dashboard cards (stats)
- `customer.html` – Customer CRUD
- `item.html` – Item CRUD
- `placeOrder.html` – Place order/invoice
- `billPrint.html` – Invoice list and print
- `js/` – All frontend JavaScript logic
- `css/` – (Optional) Custom styles
- `assets/` – Images and static assets

## 🌐 Tech Stack

- HTML5, CSS3, Bootstrap 5
- Vanilla JavaScript (ES6+)
- Backend: Java EE (Servlets), MySQL, Tomcat, Gson (see backend repo)

## ⚡ Quick Links

- [Backend Repo](../backend) (Java EE, MySQL)
- [Bootstrap Docs](https://getbootstrap.com/)

## 📝 Notes

- Update `BASE_URL` in JS files if your backend runs on a different host/port.
- All API calls expect JSON responses from the backend.

---

© 2025 Pahana-Edu Book Store
