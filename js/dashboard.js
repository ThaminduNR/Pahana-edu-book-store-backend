console.log("Dashboard")

const pages = {
    page1: `<iframe src="./home.html" style="width:100%;height:95vh;border:none;background:#fff;"></iframe>`,
    page2: `<iframe src="./customer.html" style="width:100%;height:95vh;border:none;background:#fff;"></iframe>`,
    page3: `<iframe src="./item.html" style="width:100%;height:98vh;border:none;background:#fff;"></iframe>`,
    page4: `<iframe src="./placeOrder.html" style="width:100%;height:98vh;border:none;background:#fff;"></iframe>`,
    page5: `<iframe src="./billPrint.html" style="width:100%;height:98vh;border:none;background:#fff;"></iframe>`
};
const sidebarNav = document.getElementById('sidebarNav');
const mainContent = document.getElementById('mainContent');

sidebarNav.addEventListener('click', function(e) {
    if (e.target && e.target.matches('a[data-page]')) {
        e.preventDefault();
        // Remove active from all
        sidebarNav.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
        // Add active to clicked
        e.target.classList.add('active');
        const page = e.target.getAttribute('data-page');
        mainContent.innerHTML = pages[page] || '<h2>Not Found</h2>';
    }
});