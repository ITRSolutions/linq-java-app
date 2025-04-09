$(document).ready(function() {
  

  // Resource card hover effects
  $('.resource-card').hover(
    function() {
      // Mouse enter
      $(this).find('.resource-image').css('transform', 'scale(1.05)');
      $(this).find('.resource-link i').css('transform', 'translateX(3px)');
    },
    function() {
      // Mouse leave
      $(this).find('.resource-image').css('transform', 'scale(1)');
      $(this).find('.resource-link i').css('transform', 'translateX(0)');
    }
  );

  // Responsive adjustments
  function handleResponsive() {
    if ($(window).width() > 768) {
      $('.navbar-collapse').css('display', 'flex');
    } else {
      $('.navbar-collapse').css('display', 'none');
    }
  }

  // Run on load and resize
  handleResponsive();
  $(window).resize(handleResponsive);
});