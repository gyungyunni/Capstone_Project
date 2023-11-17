let ageInput = document.getElementById('keyword-age-input');
let heightInput = document.getElementById('keyword-height-input');
let weightInput = document.getElementById('keyword-weight-input');
let genderSelect = document.getElementById('gender-select');
let activitySelect = document.getElementById('activity-select');
let purposeSelect = document.getElementById('purpose-select');
let vegetarianSelect = document.getElementById('vegetarian-select');
let searchButton = document.getElementById('search-button');

let currentSearchConditions = {
    gender: "",
    age: "",
    height:"",
    weight: "",
    activity: "",
    purpose: "",
    vegetarian: "아니요"
};

// Replace the previous fetch code with Axios
searchButton.addEventListener("click", function() {
    // Get values from form inputs
    currentSearchConditions.gender = genderSelect.value;
    currentSearchConditions.age = ageInput.value;
    currentSearchConditions.height = heightInput.value;
    currentSearchConditions.weight = weightInput.value;
    currentSearchConditions.activity = activitySelect.value;
    currentSearchConditions.purpose = purposeSelect.value;
    currentSearchConditions.vegetarian = vegetarianSelect.checked ? "네" : "아니요";

    // Send an HTTP POST request to your server using Axios
    axios.post('/api/v1/chat-gpt', new URLSearchParams(currentSearchConditions), {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
        .then(response => {
            // Handle the response from the server (response.data contains the result)
            console.log(response.data);
            // You can update the UI with the response data here
            // 식단 정보 아침 업데이트
            const morningElement = document.querySelector('.meal-table tbody tr td:nth-child(1)');
            morningElement.textContent = response.data.morning;
            // 식단 정보 점심 업데이트
            const lunchElement = document.querySelector('.meal-table tbody tr td:nth-child(2)');
            lunchElement.textContent = response.data.lunch;
            // 식단 정보 저녁 업데이트
            const dinnerElement = document.querySelector('.meal-table tbody tr td:nth-child(3)');
            dinnerElement.textContent = response.data.dinner;
        })
        .catch(error => {
            console.error('Error:', error);
        });
});