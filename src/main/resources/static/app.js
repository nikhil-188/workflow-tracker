function goToManager() {
    window.location.href = "manager.html";
}

function goToEmployee() {
    window.location.href = "employee.html";
}

function createTask(event) {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const priority = document.getElementById("priority").value;
    const assignedToUserId = document.getElementById("assignedToUserId").value;
    const dueDate = document.getElementById("dueDate").value;

    const params = new URLSearchParams();
    params.append("title", title);
    params.append("description", description);
    params.append("priority", priority);
    params.append("assignedToUserId", assignedToUserId);
    params.append("dueDate", dueDate);

    fetch("/tasks", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("result").innerText =
            "Task created with ID: " + data.id;
    })
    .catch(error => {
        document.getElementById("result").innerText =
            "Error creating task";
        console.error(error);
    });
}

function loadManagerTasks() {
    fetch("/tasks/manager/1")
        .then(response => response.json())
        .then(tasks => {
            const list = document.getElementById("taskList");
            list.innerHTML = "";

            tasks.forEach(task => {
                const li = document.createElement("li");
                li.innerText =
                    task.title +
                    " | Priority: " + task.priority +
                    " | Assigned To: " + task.assignedTo.name +
                    " | Status: " + task.status;

                list.appendChild(li);
            });
        })
        .catch(error => {
            console.error("Error loading tasks", error);
        });
}
