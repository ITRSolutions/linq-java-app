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