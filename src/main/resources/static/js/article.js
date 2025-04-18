  const itemsPerPage = 9;
  let currentPage = 0;

  $(document).ready(function () {
    const $items = $('[id^="contentBlock-"]');
    const totalPages = Math.ceil($items.length / itemsPerPage);

function showPage(page) {
  currentPage = page;
  const start = page * itemsPerPage;
  const end = start + itemsPerPage;

  $('[id^="contentBlock-"]').each(function (index) {
    if (index >= start && index < end) {
      $(this).removeClass('d-none');
    } else {
      $(this).addClass('d-none');
    }
  });

  renderPagination();
}


    function renderPagination() {
      const $pagination = $('#pagination');
      $pagination.empty();

      // Previous Button
      const prevClass = currentPage === 0 ? 'disabled' : '';
      $pagination.append(`<li class="page-item ${prevClass}">
                            <a class="page-link pagination-btn" href="#" data-page="${currentPage - 1}" aria-label="Previous">
                              <span aria-hidden="true">«</span>
                            </a>
                          </li>`);

      // Page Numbers
      for (let i = 0; i < totalPages; i++) {
        const active = i === currentPage ? 'active' : '';
        $pagination.append(`<li class="page-item ${active}">
                              <a class="page-link pagination-btn" href="#" data-page="${i}">${i + 1}</a>
                            </li>`);
      }

      // Next Button
      const nextClass = currentPage === totalPages - 1 ? 'disabled' : '';
      $pagination.append(`<li class="page-item ${nextClass}">
                            <a class="page-link pagination-btn" href="#" data-page="${currentPage + 1}" aria-label="Next">
                              <span aria-hidden="true">»</span>
                            </a>
                          </li>`);
    }

    // Initial display
    showPage(0);

    // Handle click
    $(document).on('click', '.pagination-btn', function (e) {
      e.preventDefault();
      const page = parseInt($(this).data('page'));
      if (!isNaN(page) && page >= 0 && page < totalPages) {
        showPage(page);
      }
    });
  });