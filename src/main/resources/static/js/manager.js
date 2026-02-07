const managerId = 1;

/* =========================
   Create a new task
========================= */
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
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params
    })
        .then(async res => {
            const data = await res.json();
            if (!res.ok) {
                throw data.error || "Error creating task";
            }
            return data;
        })
        .then(data => {
            output.innerText =
                `Task created successfully\n\nTask ID: ${data.id}`;
        })
        .catch(err => {
            output.innerText = err;
            console.error(err);
        });
}

/* =========================
   Fetch tasks created by manager
========================= */
function getManagerTasks() {
    fetch(`/tasks/manager/${managerId}`)
        .then(async res => {
            const data = await res.json();
            if (!res.ok) {
                throw data.error || "Error fetching tasks";
            }
            return data;
        })
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
            output.innerText = err;
            console.error(err);
        });
}

/* =========================
   Add comment to a task
========================= */
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

    fetch(`/tasks/${taskId}/comments?userId=${managerId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params
    })
        .then(async res => {
            const data = await res.json();
            if (!res.ok) {
                throw data.error;
            }
            return data;
        })
        .then(data => {
            output.innerText =
                `Comment added successfully\n\nAuthor : ${data.authorName}\nComment: ${data.content}`;
            commentContent.value = "";
        })
        .catch(err => {
            output.innerText = err;
            console.error(err);
        });
}

/* =========================
   Fetch comments for a task
========================= */
function getComments() {
    const taskId = commentTaskId.value;

    if (!taskId) {
        output.innerText = "Task ID is required";
        return;
    }

    fetch(`/tasks/${taskId}/comments?userId=${managerId}`)
        .then(async res => {
            const data = await res.json();
            if (!res.ok) {
                throw data.error;
            }
            return data;
        })
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
            output.innerText = err;
            console.error(err);
        });
}

/* =========================
   Navigation
========================= */
function goBack() {
    window.location.href = "index.html";
}
