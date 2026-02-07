const employeeId = 2;

function getEmployeeTasks() {
    fetch(`/tasks/employee/${employeeId}`)
        .then(res => res.json())
        .then(tasks => {
            if (tasks.length === 0) {
                output.innerText = "No tasks assigned.";
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
Created By  : ${task.createdBy.name}
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
    params.append("authorId", employeeId);
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
        });
}
