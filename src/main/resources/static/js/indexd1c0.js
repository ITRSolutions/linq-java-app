document.addEventListener("DOMContentLoaded", () => {
  const counters = document.querySelectorAll(".count");
  const options = {
    root: null, // Use the viewport as the container
    threshold: 0.5, // Trigger when 50% of the section is visible
  };

  const animateCount = (counter) => {
    const target = +counter.getAttribute("data-count");
    const increment = target / 100; // Adjust speed by changing denominator
    const suffix = counter.classList.contains("exception") ? "K+" : "+"; // Determine the suffix
    let current = 0;

    const updateCounter = () => {
      current += increment;
      if (current < target) {
        counter.textContent = Math.ceil(current) + suffix;
        setTimeout(updateCounter, 20); // Adjust speed by changing delay
      } else {
        counter.textContent = target + suffix; // Set the final value with the appropriate suffix
      }
    };

    updateCounter();
  };

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const counter = entry.target;
        animateCount(counter);
        observer.unobserve(counter); // Stop observing once animated
      }
    });
  }, options);

  counters.forEach((counter) => {
    observer.observe(counter);
  });




  // password toggling
  document.querySelector('.end-icon').addEventListener('click', function (e) {
    e.preventDefault();

    const input = document.querySelector('.password-input'); // Replace with your input field's ID or class

        const button = this;
        const isPassword = input.type === 'password';

        // Toggle the input type
        input.type = isPassword ? 'text' : 'password';

        // Toggle the SVG icon
        button.innerHTML = isPassword
          ? `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="16" viewBox="0 0 20 16" fill="none">
               <path d="M10 0.444444C14.5455 0.444444 18.4273 3.20889 20 7.11111C18.4273 11.0133 14.5455 13.7778 10 13.7778C5.45455 13.7778 1.57273 11.0133 0 7.11111C1.57273 3.20889 5.45455 0.444444 10 0.444444ZM10 2.22222C8.32956 2.22201 6.69256 2.68007 5.27512 3.54431C3.85769 4.40856 2.71668 5.64432 1.98182 7.11111C2.71668 8.5779 3.85769 9.81366 5.27512 10.6779C6.69256 11.5422 8.32956 12.0002 10 12C11.6704 12.0002 13.3074 11.5422 14.7249 10.6779C16.1423 9.81366 17.2833 8.5779 18.0182 7.11111C17.2833 5.64432 16.1423 4.40856 14.7249 3.54431C13.3074 2.68007 11.6704 2.22201 10 2.22222ZM10 4.44444C10.7233 4.44444 11.417 4.7254 11.9285 5.22549C12.4399 5.72559 12.7273 6.40387 12.7273 7.11111C12.7273 7.81835 12.4399 8.49663 11.9285 8.99673C11.417 9.49682 10.7233 9.77778 10 9.77778C9.2767 9.77778 8.58304 9.49682 8.07155 8.99673C7.56006 8.49663 7.27273 7.81835 7.27273 7.11111C7.27273 6.40387 7.56006 5.72559 8.07155 5.22549C8.58304 4.7254 9.2767 4.44444 10 4.44444Z" fill="#838383"/>
             </svg>`
          : `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="16" viewBox="0 0 20 16" fill="none">
               <path d="M0.909091 1.12889L2.07273 0L17.2727 14.8622L16.1182 16L13.3182 13.2622C12.2727 13.6 11.1636 13.7778 10 13.7778C5.45455 13.7778 1.57273 11.0133 0 7.11111C0.627273 5.54667 1.62727 4.16889 2.9 3.07556L0.909091 1.12889ZM10 4.44444C10.7233 4.44444 11.417 4.7254 11.9285 5.22549C12.4399 5.72559 12.7273 6.40387 12.7273 7.11111C12.7277 7.41384 12.6755 7.71443 12.5727 8L9.09091 4.59556C9.38297 4.4951 9.69039 4.444 10 4.44444ZM10 0.444444C14.5455 0.444444 18.4273 3.20889 20 7.11111C19.2576 8.95372 17.9969 10.5532 16.3636 11.7244L15.0727 10.4533C16.33 9.60306 17.3439 8.45254 18.0182 7.11111C17.2833 5.64432 16.1423 4.40856 14.7249 3.54431C13.3074 2.68007 11.6704 2.22201 10 2.22222C9.00909 2.22222 8.03636 2.38222 7.12727 2.66667L5.72727 1.30667C7.03636 0.755555 8.48182 0.444444 10 0.444444ZM1.98182 7.11111C2.71668 8.5779 3.85769 9.81366 5.27512 10.6779C6.69256 11.5422 8.32956 12.0002 10 12C10.6273 12 11.2455 11.9378 11.8182 11.8133L9.74545 9.77778C9.11287 9.71148 8.52257 9.43544 8.0727 8.99558C7.62284 8.55571 7.34053 7.97852 7.27273 7.36L4.18182 4.32889C3.28182 5.08444 2.52727 6.02667 1.98182 7.11111Z" fill="#838383"/>
             </svg>`;

  });
});


function togglePasswordVisibility(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const icon = document.getElementById(iconId);

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        icon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="16" viewBox="0 0 20 16" fill="none">
                                        <path d="M10 0.444444C14.5455 0.444444 18.4273 3.20889 20 7.11111C18.4273 11.0133 14.5455 13.7778 10 13.7778C5.45455 13.7778 1.57273 11.0133 0 7.11111C1.57273 3.20889 5.45455 0.444444 10 0.444444ZM10 2.22222C8.32956 2.22201 6.69256 2.68007 5.27512 3.54431C3.85769 4.40856 2.71668 5.64432 1.98182 7.11111C2.71668 8.5779 3.85769 9.81366 5.27512 10.6779C6.69256 11.5422 8.32956 12.0002 10 12C11.6704 12.0002 13.3074 11.5422 14.7249 10.6779C16.1423 9.81366 17.2833 8.5779 18.0182 7.11111C17.2833 5.64432 16.1423 4.40856 14.7249 3.54431C13.3074 2.68007 11.6704 2.22201 10 2.22222ZM10 4.44444C10.7233 4.44444 11.417 4.7254 11.9285 5.22549C12.4399 5.72559 12.7273 6.40387 12.7273 7.11111C12.7273 7.81835 12.4399 8.49663 11.9285 8.99673C11.417 9.49682 10.7233 9.77778 10 9.77778C9.2767 9.77778 8.58304 9.49682 8.07155 8.99673C7.56006 8.49663 7.27273 7.81835 7.27273 7.11111C7.27273 6.40387 7.56006 5.72559 8.07155 5.22549C8.58304 4.7254 9.2767 4.44444 10 4.44444Z" fill="#838383"/>
                                      </svg>`;
    } else {
        passwordInput.type = "password";
        icon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="16" viewBox="0 0 20 16" fill="none">
                                        <path d="M0.909091 1.12889L2.07273 0L17.2727 14.8622L16.1182 16L13.3182 13.2622C12.2727 13.6 11.1636 13.7778 10 13.7778C5.45455 13.7778 1.57273 11.0133 0 7.11111C0.627273 5.54667 1.62727 4.16889 2.9 3.07556L0.909091 1.12889ZM10 4.44444C10.7233 4.44444 11.417 4.7254 11.9285 5.22549C12.4399 5.72559 12.7273 6.40387 12.7273 7.11111C12.7277 7.41384 12.6755 7.71443 12.5727 8L9.09091 4.59556C9.38297 4.4951 9.69039 4.444 10 4.44444ZM10 0.444444C14.5455 0.444444 18.4273 3.20889 20 7.11111C19.2576 8.95372 17.9969 10.5532 16.3636 11.7244L15.0727 10.4533C16.33 9.60306 17.3439 8.45254 18.0182 7.11111C17.2833 5.64432 16.1423 4.40856 14.7249 3.54431C13.3074 2.68007 11.6704 2.22201 10 2.22222C9.00909 2.22222 8.03636 2.38222 7.12727 2.66667L5.72727 1.30667C7.03636 0.755555 8.48182 0.444444 10 0.444444ZM1.98182 7.11111C2.71668 8.5779 3.85769 9.81366 5.27512 10.6779C6.69256 11.5422 8.32956 12.0002 10 12C10.6273 12 11.2455 11.9378 11.8182 11.8133L9.74545 9.77778C9.11287 9.71148 8.52257 9.43544 8.0727 8.99558C7.62284 8.55571 7.34053 7.97852 7.27273 7.36L4.18182 4.32889C3.28182 5.08444 2.52727 6.02667 1.98182 7.11111Z" fill="#838383"/>
                                      </svg>`;
    }
}

// window.addEventListener('load', () => {
//   const loader = document.querySelector('.loadingio-spinner-spinner-2by998twmg8');
//   loader.classList.add('display-none') // Hides the loader
// });
