const managerId = 1; // temporary

function createTask(event) {
    event.preventDefault();

    const params = new URLSearchParams();
    params.append("title", title.value);
    params.append("description", description.value);
    params.append("priority", priority.value);
    params.append("assignedToUserId", assignedToUserId.value);
    params.append("dueDate", dueDate.value);

    fetch("/tasks", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params
    })
    .then(res => res.json())
    .then(data => {
        result.innerText = "Task created with ID: " + data.id;
        loadManagerTasks();
    });
}

function loadManagerTasks() {
    fetch(`/tasks/manager/${managerId}`)
        .then(res => res.json())
        .then(tasks => {
            taskList.innerHTML = "";
            tasks.forEach(task => {
                const li = document.createElement("li");
                li.innerText =
                    `${task.title} | ${task.priority} | ${task.status} | Assigned to: ${task.assignedTo.name}`;
                taskList.appendChild(li);
            });
        });
}
