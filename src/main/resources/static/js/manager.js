const managerId = 1;

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
    .then(data => output.innerText = JSON.stringify(data, null, 2))
    .catch(err => output.innerText = err);
}

function getManagerTasks() {
    fetch(`/tasks/manager/${managerId}`)
        .then(res => res.json())
        .then(tasks => {
            if (tasks.length === 0) {
                output.innerText = "No tasks created yet.";
                return;
            }

            let text = "";

            tasks.forEach((task, index) => {
                text += `
Task ${index + 1}
------------------
Title       : ${task.title}
Description : ${task.description}
Priority    : ${task.priority}
Due Date    : ${task.dueDate}
Assigned To : ${task.assignedTo.name}
Created At  : ${task.createdAt}

`;
            });

            output.innerText = text;
        })
        .catch(err => {
            output.innerText = "Error fetching tasks";
            console.error(err);
        });
}


function goBack() {
    window.location.href = "index.html";
}

function addComment() {
    const taskId = commentTaskId.value;
    const content = commentContent.value;

    if (!taskId || !content) {
        output.innerText = "Task ID and comment are required";
        return;
    }

    const params = new URLSearchParams();
    params.append("authorId", managerId);
    params.append("content", content);

    fetch(`/tasks/${taskId}/comments`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params
    })
    .then(res => res.json())
    .then(data => {
        output.innerText =
            `Comment added by ${data.authorName}\n\n"${data.content}"`;
        commentContent.value = "";
    })
    .catch(err => {
        output.innerText = "Error adding comment";
        console.error(err);
    });
}

function getComments() {
    const taskId = commentTaskId.value;

    if (!taskId) {
        output.innerText = "Task ID is required";
        return;
    }

    fetch(`/tasks/${taskId}/comments`)
        .then(res => res.json())
        .then(comments => {
            if (comments.length === 0) {
                output.innerText = "No comments yet";
                return;
            }

            let text = "";

            comments.forEach((c, index) => {
                text += `
Comment ${index + 1}
------------------
Author  : ${c.authorName}
Content : ${c.content}
Created : ${c.createdAt}

`;
            });

            output.innerText = text;
        })
        .catch(err => {
            output.innerText = "Error fetching comments";
            console.error(err);
        });
}
