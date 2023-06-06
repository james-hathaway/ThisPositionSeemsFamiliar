document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("username-form");
    const usernameInput = document.getElementById("username-input");
  
    const instructionsButton = document.getElementById("instructions-button");
    const closeButton = document.getElementById("close-button");
    const instructionsModal = document.getElementById("instructions-modal");
    
    form.addEventListener("submit", (event) => {
        event.preventDefault();
        const username = usernameInput.value;
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/fetch-puzzles", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onload = function() {
            if (xhr.status === 200) {
                const puzzles = JSON.parse(xhr.responseText);
                const puzzleList = document.getElementById("puzzle-list");
                puzzleList.innerHTML = "";
                puzzles.forEach((puzzle) => {
                    const puzzleItem = document.createElement("div");
                    puzzleItem.classList.add("puzzle-item");
                    puzzleItem.innerHTML = `<h3>${puzzle.title}</h3><p>${puzzle.description}</p>`;
                    puzzleList.appendChild(puzzleItem);
                });
            } else {
                console.error(`Error ${xhr.status}: ${xhr.statusText}`);
            }
        };
        xhr.onerror = function() {
            console.error('Network error');
        };
        xhr.send(JSON.stringify({ username: username }));
    });
    
    instructionsButton.addEventListener("click", (event) => {
        event.preventDefault();
        instructionsModal.style.display = "block";
    });
  
    closeButton.addEventListener("click", (event) => {
        instructionsModal.style.display = "none";
    });
  
    window.addEventListener("click", (event) => {
        if (event.target === instructionsModal) {
            instructionsModal.style.display = "none";
        }
    });
});
