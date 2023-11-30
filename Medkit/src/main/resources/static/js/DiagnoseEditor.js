let dataObject = document.getElementById("jsonData");

let diagnoseId = document.getElementById("d_id").value;

let patientSelector = document.getElementById("pat-select");
let patientNameField = document.getElementById("pat-name");

let symptomList = document.getElementById("sym-list");
let symptomSelector = document.getElementById("sym-select");

let medicineList = document.getElementById("med-list");
let medicineSelector = document.getElementById("med-select");

let diagnoseSelector = document.getElementById("disease-select");

let currentDoctor;
let currentPatient;
let currentDisease;

let allSymptoms = [];
let allMedicines = [];
let diseases = [];
let patients = [];

let currentSymptoms = [];
let currentMedicines = [];


function initialize() {
    currentDoctor = JSON.parse(dataObject.getAttribute("doctor-json"));
    currentPatient = JSON.parse(dataObject.getAttribute("patient-json"));
    currentDisease = JSON.parse(dataObject.getAttribute("disease-json"));

    allSymptoms = JSON.parse(dataObject.getAttribute("all-symptoms-json"));
    allMedicines = JSON.parse(dataObject.getAttribute("all-medicines-json"));

    currentSymptoms = JSON.parse(dataObject.getAttribute("symptoms-json"));
    currentMedicines = JSON.parse(dataObject.getAttribute("medicines-json"));

    getPatients();
    getDiseases();

    updateMedicines();
    updateSymptoms();
}

function getPatients() {
    fetch("/app/DiagnoseGetUsers", {
        method: "POST",
        headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
        body: JSON.stringify({ value: patientNameField.value })
    })
        .then(response => response.json())
        .then(data => {
            patients = data;

            updatePatientSelector();
        })
        .catch(error => {
            alert(error);

            console.error(error);
        });
}

function updatePatientSelector() {
    if (currentPatient != null)
        patientSelector.innerHTML = `<option class="c-list" value="${currentPatient.id}">${currentPatient.surname} ${currentPatient.name} ${currentPatient.patronymic}</option>`;
    else
        patientSelector.innerHTML = '';

    for (let i = 0; i < patients.length; i++) {
        patientSelector.innerHTML += `<option class="c-list" value="${patients[i].id}">${patients[i].surname} ${patients[i].name} ${patients[i].patronymic}</option>`;
    }
}

function getDiseases() {
    fetch("/app/DiagnoseGetDiseases", {
        method: "POST",
        headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
        body: JSON.stringify(currentSymptoms)
    })
        .then(response => response.json())
        .then(data => {
            diseases = data;

            updateDiseaseSelector();
        })
        .catch(error => {
            alert(error);
        });
}

function updateDiseaseSelector() {
    diagnoseSelector.innerHTML = '';
    for (let i = 0; i < diseases.length; i++) {
        diagnoseSelector.innerHTML += `<option class="c-list" value="${diseases[i].id}">${diseases[i].name}</option>`;
    }
}

function updateSymptoms() {
    symptomList.innerHTML = '';
    for (let i = 0; i < currentSymptoms.length; i++) {
        symptomList.innerHTML +=
            `<div class="symptom-element d-flex flex-row justify-content-between" style="margin-top: 16px">` +
            `<h5>${currentSymptoms[i].name}</h5>` +
            `<input style="margin-left: 16px" onclick="deleteSymptomElement(${i})" type="button" class="btn btn-danger" value="Удалить">` +
            `</div>`;
    }

    getDiseases();
}

function deleteSymptomElement(index) {
    currentSymptoms.splice(index, 1);

    updateSymptoms();
}

function insertSymptomElement() {
    let symptom;

    allSymptoms.forEach(i => {
       if (i.id == symptomSelector.value)
           symptom = i;
    });

    currentSymptoms.push(symptom);

    console.log(symptom);

    updateSymptoms();
}

function updateMedicines() {
    medicineList.innerHTML = '';
    for (let i = 0; i < currentMedicines.length; i++) {
        medicineList.innerHTML +=
            `<div class="symptom-element d-flex flex-row justify-content-between" style="margin-top: 16px">` +
            `<h5>${currentMedicines[i].name}</h5>` +
            `<input style="margin-left: 16px" onclick="deleteMedicineElement(${i})" type="button" class="btn btn-danger" value="Удалить">` +
            `</div>`;
    }
}

function deleteMedicineElement(index) {
    currentMedicines.splice(index, 1);

    updateMedicines();
}

function insertMedicineElement() {
    let symptom;

    allMedicines.forEach(i => {
        if (i.id == medicineSelector.value)
            symptom = i;
    });

    currentMedicines.push(symptom);

    console.log(symptom);

    updateMedicines();
}

function confirm(state) {
    fetch("/app/DiagnoseEditor", {
        method: "POST",
        headers: {'Content-Type': 'application/json', 'Accept': 'text/plain'},
        body: JSON.stringify( { id: diagnoseId, openDate: "", closeDate: "", note: note.value,
            description: desc.value, diseaseId: diagnoseSelector.value, patientId: patientSelector.value, doctorId: 0,
            state: state, symptomsJSON: JSON.stringify(currentSymptoms), medicinesJSON: JSON.stringify(currentMedicines)
        })
    })
        .then(response => response.text())
        .then(data => {
            if (data === "ok")
                window.location.replace("/app/MainPage");
            else
                error.innerHTML = data;
        })
        .catch(error => {
            alert(error);
        });
}