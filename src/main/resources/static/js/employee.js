const employeeId = 2; // temporary

function loadEmployeeTasks() {
    fetch(`/tasks/employee/${employeeId}`)
        .then(res => res.json())
        .then(tasks => {
            taskList.innerHTML = "";
            tasks.forEach(task => {
                const li = document.createElement("li");
                li.innerText =
                    `${task.title} | ${task.priority} | ${task.status} | Due: ${task.dueDate}`;
                taskList.appendChild(li);
            });
        });
}