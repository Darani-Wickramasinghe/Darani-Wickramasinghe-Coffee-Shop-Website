/* =====================================================================
   Coffee House – Premium Interactive Script
   ===================================================================== */

document.addEventListener('DOMContentLoaded', () => {
    /* --- State & Config --- */
    const API_BASE = 'http://127.0.0.1:8080/api';
    let isOffline = false;

    /* --- Preloader --- */
    window.addEventListener('load', () => {
        const preloader = document.getElementById('preloader');
        if (preloader) {
            setTimeout(() => {
                preloader.classList.add('done');
                document.body.classList.remove('no-scroll');
            }, 600); // slight delay for effect
        }
    });

    /* --- Navigation & Mobile Menu --- */
    const header = document.getElementById('site-header');
    const navMenu = document.getElementById('nav-menu');
    const openBtn = document.getElementById('menu-open-button');
    const closeBtn = document.getElementById('menu-close-button');
    const navLinks = document.querySelectorAll('.nav-link');

    // Scroll effect
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
        updateActiveLink();
        updateBackToTop();
    });

    // Mobile menu toggles
    if (openBtn) openBtn.addEventListener('click', () => navMenu.classList.add('open'));
    if (closeBtn) closeBtn.addEventListener('click', () => navMenu.classList.remove('open'));

    // Close mobile menu on link click
    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            navMenu.classList.remove('open');
        });
    });

    // Active link highlighting
    const sections = document.querySelectorAll('section[id]');
    function updateActiveLink() {
        let scrollY = window.scrollY + 200; // offset
        sections.forEach(current => {
            const sectionHeight = current.offsetHeight;
            const sectionTop = current.offsetTop - 100;
            const sectionId = current.getAttribute('id');
            const link = document.querySelector(`.nav-menu a[href*=${sectionId}]`);
            
            if (link) {
                if (scrollY > sectionTop && scrollY <= sectionTop + sectionHeight) {
                    navLinks.forEach(l => l.classList.remove('active'));
                    link.classList.add('active');
                }
            }
        });
    }

    /* --- Scroll Reveal Animations --- */
    const revealElements = document.querySelectorAll('.reveal, .reveal-left, .reveal-right');
    const revealObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                // stop observing once revealed
                revealObserver.unobserve(entry.target);
            }
        });
    }, { threshold: 0.15, rootMargin: "0px 0px -50px 0px" });

    revealElements.forEach(el => revealObserver.observe(el));

    /* --- Stats Counter Animation --- */
    const statNums = document.querySelectorAll('.stat-num');
    const statsObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const el = entry.target;
                const target = parseInt(el.getAttribute('data-target'));
                let current = 0;
                const duration = 2000;
                const increment = target / (duration / 16); // 60fps

                const updateCounter = () => {
                    current += increment;
                    if (current < target) {
                        el.innerText = Math.ceil(current).toLocaleString() + (target > 1000 ? '+' : '');
                        requestAnimationFrame(updateCounter);
                    } else {
                        el.innerText = target.toLocaleString() + (target > 1000 ? '+' : '');
                    }
                };
                updateCounter();
                statsObserver.unobserve(el);
            }
        });
    }, { threshold: 0.5 });

    statNums.forEach(num => statsObserver.observe(num));

    /* --- Menu Filtering --- */
    const filterBtns = document.querySelectorAll('.filter-btn');
    const menuCards = document.querySelectorAll('.menu-card');

    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            // Update active button
            filterBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const filterValue = btn.getAttribute('data-filter');

            menuCards.forEach(card => {
                // Remove visible class for re-animation
                card.classList.remove('visible');
                
                if (filterValue === 'all' || card.getAttribute('data-category') === filterValue) {
                    card.classList.remove('hidden');
                    // slight delay to re-trigger animation
                    setTimeout(() => card.classList.add('visible'), 50);
                } else {
                    card.classList.add('hidden');
                }
            });
        });
    });

    /* --- Gallery Lightbox --- */
    const lightbox = document.getElementById('lightbox');
    const lbImg = document.getElementById('lb-img');
    const lbCaption = document.getElementById('lb-caption');
    const lbClose = document.getElementById('lb-close');
    const lbNext = document.getElementById('lb-next');
    const lbPrev = document.getElementById('lb-prev');
    const galleryItems = document.querySelectorAll('.gallery-item');
    let currentImageIndex = 0;

    function openLightbox(index) {
        currentImageIndex = index;
        const item = galleryItems[index];
        const img = item.querySelector('img');
        lbImg.src = img.src;
        lbCaption.innerText = img.alt;
        lightbox.style.display = 'flex';
        document.body.style.overflow = 'hidden'; // prevent scrolling
    }

    function closeLightbox() {
        lightbox.style.display = 'none';
        document.body.style.overflow = 'auto';
    }

    function showNextImage() {
        currentImageIndex = (currentImageIndex + 1) % galleryItems.length;
        openLightbox(currentImageIndex);
    }

    function showPrevImage() {
        currentImageIndex = (currentImageIndex - 1 + galleryItems.length) % galleryItems.length;
        openLightbox(currentImageIndex);
    }

    galleryItems.forEach((item, index) => {
        item.addEventListener('click', () => openLightbox(index));
    });

    if (lbClose) lbClose.addEventListener('click', closeLightbox);
    if (lbNext) lbNext.addEventListener('click', showNextImage);
    if (lbPrev) lbPrev.addEventListener('click', showPrevImage);

    // Close on overlay click
    if (lightbox) {
        lightbox.addEventListener('click', (e) => {
            if (e.target === lightbox) closeLightbox();
        });
    }

    // Keyboard navigation
    document.addEventListener('keydown', (e) => {
        if (lightbox && lightbox.style.display === 'flex') {
            if (e.key === 'Escape') closeLightbox();
            if (e.key === 'ArrowRight') showNextImage();
            if (e.key === 'ArrowLeft') showPrevImage();
        }
    });

    /* --- Toast Notification --- */
    function showToast(message, type = 'success') {
        let toast = document.getElementById('global-toast');
        if (!toast) {
            toast = document.createElement('div');
            toast.id = 'global-toast';
            toast.className = 'toast';
            document.body.appendChild(toast);
        }
        
        // Reset classes
        toast.className = 'toast';
        if (type === 'error') {
            toast.style.background = '#e53e3e';
            toast.style.color = '#fff';
        } else if (type === 'offline') {
            toast.style.background = '#d97f10';
            toast.style.color = '#fff';
        } else {
            toast.style.background = 'var(--clr-primary-dark)';
            toast.style.color = 'var(--clr-white)';
        }

        toast.innerText = message;
        
        // Force reflow
        void toast.offsetWidth;
        
        toast.classList.add('show');
        
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3500);
    }

    /* --- Backend Connectivity Check --- */
    async function checkBackend() {
        try {
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), 3000);
            const res = await fetch(`${API_BASE}/menu`, { signal: controller.signal });
            clearTimeout(timeoutId);
            isOffline = !res.ok;
        } catch (error) {
            isOffline = true;
            console.warn("Backend is unreachable. Switching to offline mode.");
        }
    }
    
    // Check on load
    checkBackend();

    /* --- Contact Form Handling --- */
    const contactForm = document.getElementById('contact-form');
    if (contactForm) {
        contactForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const btn = document.getElementById('contact-submit');
            
            const name = document.getElementById('contact-name').value.trim();
            const email = document.getElementById('contact-email').value.trim();
            const subject = document.getElementById('contact-subject').value.trim();
            const message = document.getElementById('contact-message').value.trim();

            if (!name || !email || !message) {
                showToast("Please fill in all required fields.", "error");
                return;
            }

            btn.disabled = true;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';

            if (isOffline) {
                // Mailto fallback
                setTimeout(() => {
                    const mailtoLink = `mailto:info@coffeeshopwebsite.com?subject=Contact from ${name}: ${subject}&body=${encodeURIComponent(message)}%0D%0A%0D%0AFrom: ${name} (${email})`;
                    window.location.href = mailtoLink;
                    showToast("Redirecting to email client (Offline Mode)", "offline");
                    contactForm.reset();
                    btn.disabled = false;
                    btn.innerHTML = '<i class="fas fa-paper-plane"></i> Send Message';
                }, 800);
                return;
            }

            try {
                const res = await fetch(`${API_BASE}/contact`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ name, email, subject, message })
                });

                if (res.ok) {
                    showToast("Message sent successfully!");
                    contactForm.reset();
                } else {
                    throw new Error('Server error');
                }
            } catch (err) {
                showToast("Failed to connect to server.", "error");
            } finally {
                btn.disabled = false;
                btn.innerHTML = '<i class="fas fa-paper-plane"></i> Send Message';
            }
        });
    }

    /* --- Order Modal Handling (Dynamic Creation) --- */
    window.openOrderModal = function(itemName) {
        // Create modal HTML dynamically
        const modalHtml = `
            <div id="order-modal-wrap" class="modal-overlay">
                <div class="modal-box">
                    <div class="modal-head">
                        <h3>Complete Your Order</h3>
                        <button class="modal-x" onclick="closeOrderModal()"><i class="fas fa-times"></i></button>
                    </div>
                    <form id="order-form">
                        <div class="form-group">
                            <label>Selected Item</label>
                            <input type="text" id="order-item" class="form-ctrl" value="${itemName}" readonly>
                        </div>
                        <div class="form-group">
                            <label>Your Name *</label>
                            <input type="text" id="order-name" class="form-ctrl" required>
                        </div>
                        <div class="form-group">
                            <label>Your Email *</label>
                            <input type="email" id="order-email" class="form-ctrl" required>
                        </div>
                        <div class="form-group">
                            <label>Quantity *</label>
                            <input type="number" id="order-qty" class="form-ctrl" min="1" value="1" required>
                        </div>
                        <div class="modal-btns">
                            <button type="button" class="btn btn-cancel" onclick="closeOrderModal()">Cancel</button>
                            <button type="submit" id="order-submit-btn" class="btn btn-primary">Confirm Order</button>
                        </div>
                    </form>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalHtml);
        document.body.classList.add('no-scroll');

        const orderForm = document.getElementById('order-form');
        orderForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const btn = document.getElementById('order-submit-btn');
            
            const customerName = document.getElementById('order-name').value;
            const customerEmail = document.getElementById('order-email').value;
            const item = document.getElementById('order-item').value;
            const qty = document.getElementById('order-qty').value;
            
            // Basic price estimation (replace with actual lookup if needed)
            const estimatedPrice = 500; 
            const totalAmount = estimatedPrice * qty;

            const orderData = {
                customerName,
                customerEmail,
                items: `${qty}x ${item}`,
                totalAmount
            };

            btn.disabled = true;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';

            if (isOffline) {
                // LocalStorage fallback
                setTimeout(() => {
                    const offlineOrders = JSON.parse(localStorage.getItem('offlineOrders') || '[]');
                    offlineOrders.push({...orderData, date: new Date().toISOString()});
                    localStorage.setItem('offlineOrders', JSON.stringify(offlineOrders));
                    
                    showToast("Order saved offline. Will sync when online.", "offline");
                    closeOrderModal();
                }, 800);
                return;
            }

            try {
                const res = await fetch(`${API_BASE}/orders`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(orderData)
                });

                if (res.ok) {
                    showToast("Order placed successfully!");
                    closeOrderModal();
                } else {
                    throw new Error('Server rejected order');
                }
            } catch (err) {
                showToast("Failed to place order. Try again later.", "error");
                btn.disabled = false;
                btn.innerHTML = 'Confirm Order';
            }
        });
    };

    window.closeOrderModal = function() {
        const modal = document.getElementById('order-modal-wrap');
        if (modal) {
            modal.remove();
            // Only remove no-scroll if lightbox isn't open
            if (!lightbox || lightbox.style.display !== 'flex') {
                document.body.classList.remove('no-scroll');
            }
        }
    };

    // Hero order button hook
    const heroOrderBtn = document.getElementById('order-now-btn-hero');
    const navOrderBtn = document.getElementById('order-now-btn');
    const offerOrderBtn = document.getElementById('offer-order-btn');

    [heroOrderBtn, navOrderBtn, offerOrderBtn].forEach(btn => {
        if(btn) {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                window.openOrderModal('Custom Request (via General Order)');
            });
        }
    });

    /* --- Back to Top --- */
    const backToTopBtn = document.getElementById('back-to-top');
    function updateBackToTop() {
        if (backToTopBtn) {
            if (window.scrollY > 400) {
                backToTopBtn.classList.add('visible');
            } else {
                backToTopBtn.classList.remove('visible');
            }
        }
    }

    if (backToTopBtn) {
        backToTopBtn.addEventListener('click', () => {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }
});