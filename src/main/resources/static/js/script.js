'use strict';

// DOM

const taskUrl = "http://"+window.location.hostname+":8080/task";
const assigneeUrl = "http://"+window.location.hostname+":8080/assignee";

const _body = document.querySelector('body');
const _lightButtonList = document.querySelectorAll('.btn-light');
const _darkButtonList = document.querySelectorAll('.btn-dark');
const _greyButtonList = document.querySelectorAll('.btn-secondary');
const _colourToggleButton = document.querySelector('#colourToggleButton');
const _refreshButton = document.querySelector('#refreshButton');
let _accordionButtonList;
let _modalContentList;
let _modalFormControlList;
let _modalFormSelectList;
const _modalCloseButtonList = document.querySelectorAll('.modal-header .btn-close');

const _tdlAccordion = document.querySelector("#tdlAccordionFlush");
const _newTaskModal = document.querySelector("#newTaskModal");
const _newAssigneeModal = document.querySelector("#newAssigneeModal");
const _editAssigneeModal = document.querySelector("#editAssigneeModal");
const _editTaskModal = document.querySelector("#editTaskModal");
const _addAssigneeToTaskModal = document.querySelector("#addAssigneeToTaskModal");

const _newTaskTitle = document.querySelector("#newTaskTitle");
const _editTaskTitle = document.querySelector("#editTaskTitle");
const _editTaskRemoveAssigneeSelect = document.querySelector("#editTaskRemoveAssigneeSelect");

const _editTaskSubmit = document.querySelector("#editTaskSubmit");
const _addAssigneeToTaskSubmit = document.querySelector("#addAssigneeToTaskSubmit");
const _editTaskRemoveAssigneeButton = document.querySelector("#editTaskRemoveAssigneeButton");

const _newAssigneeName = document.querySelector("#newAssigneeName");
const _editAssigneeName = document.querySelector("#editAssigneeName");
const _addAssigneeToTaskSelect = document.querySelector("#addAssigneeToTaskSelect");
const _editAssigneeSelect = document.querySelector("#editAssigneeSelect");

// Dark mode

let darkMode = false;

const lightDarkButtonToggle = (elem) => {
    elem.classList.toggle('btn-dark');
    elem.classList.toggle('btn-light');
}

const greyLightButtonToggle = (elemList) => {
    if (elemList.length != 0) {
        elemList.forEach(btn => {
            btn.classList.toggle('btn-secondary');
            btn.classList.toggle('btn-light');
        });
    }
}

const listColourToggle = () => {
    greyLightButtonToggle(_greyButtonList);
    greyLightButtonToggle(_lightButtonList);

    lightDarkButtonToggle(_colourToggleButton);
    lightDarkButtonToggle(_refreshButton);
};

const modalColourToggle = () => {
    _modalContentList.forEach((elem) => {
        elem.classList.toggle('bg-dark');
    });

    _modalFormControlList.forEach((elem) => {
        elem.classList.toggle('bg-dark');
        elem.classList.toggle('text-light');
    });

    _modalFormSelectList.forEach((elem) => {
        elem.classList.toggle('bg-dark');
        elem.classList.toggle('text-light');
    });

    _modalCloseButtonList.forEach((elem) => {
        elem.classList.toggle('btn-close-white');
    })
};

const accordionButtonColourToggle = () => {
    _accordionButtonList = document.querySelectorAll('.accordion-button')
    _modalContentList = document.querySelectorAll('.modal-content');
    _modalFormControlList = document.querySelectorAll('.modal-content .form-control');
    _modalFormSelectList = document.querySelectorAll('.modal-content .form-select');

    _accordionButtonList.forEach((elem) => elem.classList.toggle('text-white'));

    for (let i = 0; i < _accordionButtonList.length - 1; i++) {
        _accordionButtonList[i].classList.toggle('border-bottom');
        _accordionButtonList[i].classList.toggle('border-secondary');
    }
};

const bodyColourToggle = () => {
    _body.classList.toggle('bg-dark');
    _body.classList.toggle('text-white');
};

const colourToggle = () => {
    if (!darkMode) {
        bodyColourToggle();
        listColourToggle();
        accordionButtonColourToggle();
        modalColourToggle();
        darkMode = true;
    } else {
        bodyColourToggle();
        listColourToggle();
        accordionButtonColourToggle();
        modalColourToggle();
        darkMode = false;
    }
};

// Task

const readAllTasks = () => {
    fetch(`${taskUrl}/read/all`)
        .then(response => response.json())
        .then(tasks => {
            const getAssigneesFromTask = (assignees) => {
                let result = "";

                if (assignees.length > 0) {
                    result = "Assigned to: ";
                }

                for (let i = 0; i < assignees.length; i++) {
                    result = result + assignees[i].name;

                    if (i !== assignees.length - 1) {
                        result = result + ", "
                    }
                }

                return result;
            };

            _tdlAccordion.innerHTML = "";

            if (tasks.length != 0) {
                for (let i = 0; i < tasks.length; i++) {

                    _tdlAccordion.innerHTML = _tdlAccordion.innerHTML +
                        `<div class="accordion-item">
                <h2 class="accordion-header" id="flush-heading${i}">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                        data-bs-target="#flush-collapse${i}" aria-expanded="false"
                        aria-controls="flush-collapse${i}">
                        <div class="d-flex flex-column text-start">
                            <span class="task-title">${getTitleFromTask(tasks[i].title, tasks[i].completed)}</span>
                            <span class="assigned-to">${getAssigneesFromTask(tasks[i].assignees)}</span>
                        </div>
                    </button>
                </h2>
                <div id="flush-collapse${i}" class="accordion-collapse collapse"
                    aria-labelledby="flush-heading${i}" data-bs-parent="#tdlAccordionFlush">
                    <div class="accordion-body d-flex flex-column text-start">
                        <p class="date-time-stamp">Added: ${tasks[i].dateTimeSet}</p>
                        <div class="task-edit-buttons d-flex justify-content-between">
                            <button type="button" class="btn btn-outline-primary btn-sm"
                                data-bs-toggle="modal" data-bs-target="#editTaskModal" 
                                data-bs-id="${tasks[i].id}">Edit task</button>
                            <button type="button" class="btn btn-outline-primary btn-sm"
                                data-bs-toggle="modal" data-bs-target="#addAssigneeToTaskModal" 
                                data-bs-id="${tasks[i].id}">Add assignee</button>
                            <button type="button"
                                class="btn btn-outline-primary btn-sm" onclick="toggleCompletedTask(${tasks[i].id}, ${tasks[i].completed})">Completed?</button>
                            <button type="button" class="btn btn-danger" onclick="deleteTask(${tasks[i].id})">Delete</button>
                        </div>
                    </div>
                </div>
            </div>`;

                }
            } else {
                _tdlAccordion.innerHTML = `<p class="text-black-50 text-center"><em>No tasks to show!</em></p>`;
            }

            if (darkMode) {
                accordionButtonColourToggle();
            }
        })
        .catch(err => console.error(`error ${err}`));
};

const readByIdTask = (taskId) => {
    fetch(`${taskUrl}/read/${taskId}`)
        .then(response => response.json())
        .then(model => {
            console.log("readByIdTask:")
            console.log(model);
            return model;
        })
        .catch(err => console.error(`error ${err}`));
}

const toggleCompletedTask = (taskId, completed) => {
    let data = {
        "completed": completed ? false : true
    }

    fetch(`${taskUrl}/update/${taskId}`, {
        method: "PUT",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(model => {
            console.log(model);
            readAllTasks();
        })
        .catch(err => console.error(`error ${err}`));
}

const createTask = () => {
    const newTaskTitle = _newTaskTitle.value;

    let data = {
        "title": newTaskTitle,
        "completed": false
    }

    fetch(`${taskUrl}/create`, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(model => {
            console.log(model);
            readAllTasks();
        })
        .catch(err => console.error(`error ${err}`));
};

const deleteTask = (taskId) => {
    fetch(`${taskUrl}/delete/${taskId}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => console.log(response))
        .then(() => readAllTasks())
        .catch(err => console.error(`error ${err}`));
};

const getTitleFromTask = (taskTitle, completed) => {
    return completed ? `<span class="text-decoration-line-through">${taskTitle}</span>` : taskTitle;
};

const populateTaskAssignees = (taskId) => {
    fetch(`${taskUrl}/read/${taskId}`)
        .then(response => response.json())
        .then(model => {
            _editTaskRemoveAssigneeSelect.innerHTML = "";
            let assignees = model.assignees;
            if (assignees.length !== 0) {
                for (let i = 0; i < assignees.length; i++) {
                    _editTaskRemoveAssigneeSelect.innerHTML =
                        `${_editTaskRemoveAssigneeSelect.innerHTML} <option value="${assignees[i].id}">${assignees[i].name}</option>`;
                }
            }
        })
        .catch(err => console.error(`error ${err}`));
};

_newTaskModal.addEventListener('show.bs.modal', (event) => {
    _newTaskTitle.value = "";
});

_newAssigneeModal.addEventListener('show.bs.modal', (event) => {
    _newAssigneeName.value = "";
});

_editAssigneeModal.addEventListener('show.bs.modal', (event) => {
    _editAssigneeName.value = "";
});

_editTaskModal.addEventListener('show.bs.modal', (event) => {
    let button = event.relatedTarget;
    let taskId = button.getAttribute('data-bs-id');
    _editTaskSubmit.setAttribute('data-bs-id', taskId);
    _editTaskRemoveAssigneeButton.setAttribute('data-bs-id', taskId);
    _editTaskTitle.value = "";
    populateTaskAssignees(taskId);
});

_addAssigneeToTaskModal.addEventListener('show.bs.modal', (event) => {
    let button = event.relatedTarget;
    let taskId = button.getAttribute('data-bs-id');
    _addAssigneeToTaskSubmit.setAttribute('data-bs-id', taskId);
});

_editTaskSubmit.addEventListener('click', () => {
    let taskId = _editTaskSubmit.getAttribute('data-bs-id');
    let title = _editTaskTitle.value;

    let data = {
        "title": title
    }

    fetch(`${taskUrl}/update/${taskId}`, {
        method: "PUT",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(model => {
            console.log(model);
            readAllTasks();
        })
        .catch(err => console.error(`error ${err}`));
});

_addAssigneeToTaskSubmit.addEventListener('click', () => {
    let taskId = _addAssigneeToTaskSubmit.getAttribute('data-bs-id');
    let assigneeId = _addAssigneeToTaskSelect.value;

    fetch(`${taskUrl}/update/${taskId}/add-assignee?assignee_id=${assigneeId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(model => {
            console.log(model);
            readAllTasks();
        })
        .catch(err => console.error(`error ${err}`));
});

_editTaskRemoveAssigneeButton.addEventListener('click', () => {
    let taskId = _editTaskRemoveAssigneeButton.getAttribute('data-bs-id');
    let assigneeId = _editTaskRemoveAssigneeSelect.value;

    fetch(`${taskUrl}/update/${taskId}/remove-assignee?assignee_id=${assigneeId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(model => {
            console.log(model);
            readAllTasks();
            populateTaskAssignees(taskId);
        })
        .catch(err => console.error(`error ${err}`));
});


// Assignee

const populateAssignees = () => {
    _addAssigneeToTaskSelect.innerHTML = '';
    _editAssigneeSelect.innerHTML = '';

    fetch(`${assigneeUrl}/read/all`)
        .then(response => response.json())
        .then(assignees => {
            if (assignees.length != 0) {
                for (let i = 0; i < assignees.length; i++) {
                    _addAssigneeToTaskSelect.innerHTML =
                        `${_addAssigneeToTaskSelect.innerHTML} <option value="${assignees[i].id}">${assignees[i].name}</option>`;
                    _editAssigneeSelect.innerHTML =
                        `${_editAssigneeSelect.innerHTML} <option value="${assignees[i].id}">${assignees[i].name}</option>`;
                }
            }
        })
        .catch(err => console.error(`error ${err}`));
};

const createAssignee = () => {
    const newAssigneeName = _newAssigneeName.value;

    let data = {
        "name": newAssigneeName
    }

    fetch(`${assigneeUrl}/create`, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(model => {
            console.log(model);
            populateAssignees();
        })
        .catch(err => console.error(`error ${err}`));
};

const updateAssignee = () => {
    const editAssigneeId = _editAssigneeSelect.value;
    const editAssigneeName = _editAssigneeName.value;

    let data = {
        "name": editAssigneeName
    }

    fetch(`${assigneeUrl}/update?id=${editAssigneeId}`, {
        method: "PUT",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(model => {
            console.log(model)
            refresh();
        })
        .catch(err => console.error(`error ${err}`));
};

const deleteAssignee = () => {
    const editAssigneeId = _editAssigneeSelect.value;

    fetch(`${assigneeUrl}/delete/${editAssigneeId}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => console.log(response))
        .then(() => {
            console.log("Delete successful");
            refresh();
        })
        .catch(err => console.error(`error ${err}`));
};

const refresh = () => {
    readAllTasks();
    populateAssignees();
};

refresh();