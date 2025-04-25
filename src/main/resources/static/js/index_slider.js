document.addEventListener('DOMContentLoaded', function() {
  // Initialize carousel with auto-scrolling
  var myCarousel = new bootstrap.Carousel(document.getElementById('carouselExampleCaptions'), {
    interval: 4000,  // 4 seconds between slides
    wrap: true,      // Infinite looping
    pause: false    // Disable pause on hover
  });
});