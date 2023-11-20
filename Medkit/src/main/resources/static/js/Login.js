function onLoginSubmit() {
    fetch("/api/login", {
        method: "POST", mode: 'no-cors',
        headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
        body: JSON.stringify({ login: name_field.value, password: password_field.value })
    })
        .then(response => response.text())
        .then(data => {
            const testElement = document.getElementById("test");

            testElement.innerText = data;
        })
        .catch(error => {
            alert(error);
        });
}