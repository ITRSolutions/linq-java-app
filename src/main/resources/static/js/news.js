$(document).ready(function() {
  // Initialize hover effects
  function initHoverEffects() {
      $('.news-link').hover(
          function() {
              // Mouse enter
              $(this).find('.news-card').css('transform', 'translateY(-5px)');
              $(this).find('.card-img').css('transform', 'scale(1.05)');
              $(this).find('.hover-line').css('width', '100%');
          },
          function() {
              // Mouse leave
              $(this).find('.news-card').css('transform', 'translateY(0)');
              $(this).find('.card-img').css('transform', 'scale(1)');
              $(this).find('.hover-line').css('width', '0');
          }
      );
  }

  // Initialize on page load
  initHoverEffects();

  // Reinitialize on window resize
  $(window).resize(function() {
      initHoverEffects();
  });

  // Pagination active state handling
  $('.pagination-btn').click(function(e) {
      e.preventDefault();
      $('.pagination-btn').parent().removeClass('active');
      $(this).parent().addClass('active');
  });
});


    const newsData = JSON.parse($('#articleDataJson').html());
    const newsArray = Object.values(newsData);

    const itemsPerPage = 6;
    const itemsPerRow = 3; // 1 large + 2 small
    const rowsPerPage = 2;

    const container = $('.news-events-container .container-fluid:first');
    const pagination = $('#pagination');

    function largeNewsDiv(news) {
        return `
        <a href="/blog?page=${news.url}" class="news-link h-100">
            <div class="card h-100 border-0 shadow-sm overflow-hidden news-card" style="transform: translateY(-5px);">
                <div class="ratio ratio-16x9 h-100">
                    <img src="${news.image}" class="card-img object-fit-cover" alt="News Image" style="transform: scale(1.05);">
                    <div class="card-img-overlay d-flex flex-column justify-content-end p-4">
                        <div class="news-date text-white mb-2">${news.date}</div>
                        <h3 class="news-title text-white position-relative">${news.title}<span class="hover-line" style="width: 100%;"></span></h3>
                    </div>
                </div>
            </div>
        </a>`;
    }

    function smallNewsDiv(news) {
        return `
        <div class="col-12">
            <a href="/blog?page=${news.url}" class="news-link h-100">
                <div class="card h-100 border-0 shadow-sm overflow-hidden news-card" style="transform: translateY(0px);">
                    <div class="ratio ratio-16x9 h-100">
                        <img src="${news.image}" class="card-img object-fit-cover" alt="News Image" style="transform: scale(1);">
                        <div class="card-img-overlay d-flex flex-column justify-content-end p-3">
                            <div class="news-date text-white mb-2">${news.date}</div>
                            <h3 class="news-title text-white position-relative">${news.title}<span class="hover-line" style="width: 0;"></span></h3>
                        </div>
                    </div>
                </div>
            </a>
        </div>`;
    }

    function renderPage(page) {
        container.find('.row.gx-4.gy-4.mb-4').remove(); // Clear old content

        const start = page * itemsPerPage;
        const end = Math.min(start + itemsPerPage, newsArray.length);
        const chunk = newsArray.slice(start, end);

        for (let i = 0; i < chunk.length; i += itemsPerRow) {
            const large = chunk[i];
            const small1 = chunk[i + 1];
            const small2 = chunk[i + 2];

            const rowHTML = `
            <div class="row gx-4 gy-4 mb-4">
                <div class="col-xl-8 col-lg-7 col-md-6 col-12 large-news">
                    ${large ? largeNewsDiv(large) : ''}
                </div>
                <div class="col-xl-4 col-lg-5 col-md-6 col-12 small-news">
                    <div class="row g-4 h-100">
                        ${small1 ? smallNewsDiv(small1) : ''}
                        ${small2 ? smallNewsDiv(small2) : ''}
                    </div>
                </div>
            </div>`;
            container.append(rowHTML);
        }
    }

    function renderPagination(currentPage, totalPages) {
        let html = '';

        // Previous button
        html += `<li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                    <a class="page-link pagination-btn" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                        <span aria-hidden="true">«</span>
                    </a>
                </li>`;

        // Page numbers
        for (let i = 0; i < totalPages; i++) {
            html += `<li class="page-item ${i === currentPage ? 'active' : ''}">
                        <a class="page-link pagination-btn" href="#" data-page="${i}">${i + 1}</a>
                     </li>`;
        }

        // Next button
        html += `<li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
                    <a class="page-link pagination-btn" href="#" data-page="${currentPage + 1}" aria-label="Next">
                        <span aria-hidden="true">»</span>
                    </a>
                </li>`;

        pagination.html(html);
    }

    // Init pagination
    const totalPages = Math.ceil(newsArray.length / itemsPerPage);
    let currentPage = 0;

    function updatePage(page) {
        currentPage = page;
        renderPage(currentPage);
        renderPagination(currentPage, totalPages);
    }

    // Initial render
    updatePage(0);

    // Event delegation for pagination
    pagination.on('click', '.pagination-btn', function (e) {
        e.preventDefault();
        const page = parseInt($(this).data('page'));
        if (!isNaN(page) && page >= 0 && page < totalPages) {
            updatePage(page);
        }
    });
