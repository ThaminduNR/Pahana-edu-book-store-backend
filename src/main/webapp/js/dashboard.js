console.log("Dashboard")

const pages = {
    page1: `<h2>Home</h2><p>This is the home page content.</p>`,
    page2: `<iframe src="./customer.html" style="width:100%;height:95vh;border:none;background:#fff;"></iframe>`,
    page3: `<h2>Settings</h2><p>Adjust your settings here.</p>`,
    page4: `<h2>Reports</h2><p>View your reports here.</p>`,
    page5: `<h2>Support</h2><p>Contact support here.</p>`
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
        // Load content
        const page = e.target.getAttribute('data-page');
        mainContent.innerHTML = pages[page] || '<h2>Not Found</h2>';
    }
});