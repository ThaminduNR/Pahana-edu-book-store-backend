console.log("Dashboard")

const pages = {
    page1: `<h2>Home</h2><p>This is the home page content.</p>`,
    page2: `<iframe src="./customer.html" style="width:100%;height:95vh;border:none;background:#fff;"></iframe>`,
    page3: `<iframe src="./item.html" style="width:100%;height:98vh;border:none;background:#fff;"></iframe>`,
    page4: `<iframe src="./placeOrder.html" style="width:100%;height:98vh;border:none;background:#fff;"></iframe>`,
    page5: `<h2>Support</h2><p>Contact support here.</p>`
};
const sidebarNav = document.getElementById('sidebarNav');
const mainContent = document.getElementById('mainContent');
const mainIframe = document.getElementById('mainIframe');
const mainWelcome = document.getElementById('mainWelcome');
sidebarNav.addEventListener('click', function(e) {
    if (e.target && e.target.matches('a[data-page]')) {
        e.preventDefault();
        // Remove active from all
        sidebarNav.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
        // Add active to clicked
        e.target.classList.add('active');
        const page = e.target.getAttribute('data-page');
        if (page === 'page2') {
            mainIframe.src = './customer.html';
            mainIframe.style.display = 'block';
            mainWelcome.style.display = 'none';
        } else if (page === 'page3') {
            mainIframe.src = './item.html';
            mainIframe.style.display = 'block';
            mainWelcome.style.display = 'none';
        } else {
            mainIframe.style.display = 'none';
            mainWelcome.style.display = 'block';
            mainWelcome.innerHTML = pages[page] || '<h2>Not Found</h2>';
        }
    }
});