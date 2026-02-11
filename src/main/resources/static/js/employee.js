// Global State
let currentEmployeeId = localStorage.getItem('userId');
let currentOpenTaskId = null;

// Auth Check
if (!currentEmployeeId || localStorage.getItem('role') !== 'EMPLOYEE') {
    window.location.href = 'index.html';
}

document.addEventListener('DOMContentLoaded', () => {
    fetchTasks();
});

function logout() {
    localStorage.clear();
    window.location.href = 'index.html';
}

/* ---------------- API Calls ---------------- */

async function fetchTasks() {
    try {
        const response = await fetch(`/tasks/employee/${currentEmployeeId}`);
        if (!response.ok) throw new Error('Failed to fetch tasks');

        let tasks = await response.json();
        renderTaskList(tasks);
    } catch (error) {
        console.error(error);
        document.getElementById('taskList').innerHTML =
            `<div class="card" style="text-align: center; color: var(--danger); grid-column: 1/-1;">Error loading tasks.</div>`;
    }
}

// Global tasks list for quick access (since we don't have getTaskById)
let allTasks = [];

function renderTaskList(tasks) {
    allTasks = tasks;
    const container = document.getElementById('taskList');

    if (tasks.length === 0) {
        container.innerHTML = `<div class="card" style="text-align: center; grid-column: 1/-1;">No tasks assigned to you.</div>`;
        return;
    }

    container.innerHTML = tasks.map(task => `
        <div class="card task-card" onclick="showTaskDetail(${task.id})">
            <div class="flex justify-between" style="margin-bottom: 0.5rem">
                <span class="badge ${getPriorityBadgeClass(task.priority)}">${task.priority}</span>
                <span class="badge badge-status">${task.status}</span>
            </div>
            <h3>${task.title}</h3>
            <p class="text-sm" style="margin-bottom: 0;">Due: ${task.dueDate}</p>
        </div>
    `).join('');
}

function getPriorityBadgeClass(priority) {
    switch (priority) {
        case 'HIGH': return 'badge-high';
        case 'MEDIUM': return 'badge-medium';
        case 'LOW': return 'badge-low';
        default: return 'badge-status';
    }
}

function showTaskDetail(taskId) {
    const task = allTasks.find(t => t.id === taskId);
    if (!task) return;

    currentOpenTaskId = taskId;

    // Populate Modal
    document.getElementById('detailTitle').innerText = task.title;
    document.getElementById('detailDescription').innerText = task.description;
    document.getElementById('detailPriority').innerText = task.priority;
    document.getElementById('detailPriority').className = `badge ${getPriorityBadgeClass(task.priority)}`;
    document.getElementById('detailStatus').innerText = task.status;
    document.getElementById('detailDueDate').innerText = task.dueDate;

    // Fetch Comments
    fetchComments(taskId);

    openModal('taskDetailsModal');
}

/* ---------------- Comments ---------------- */

async function fetchComments(taskId) {
    const list = document.getElementById('commentsList');
    list.innerHTML = '<p class="text-sm">Loading comments...</p>';

    try {
        // Fetch comments as Employee (pass userId query param)
        const response = await fetch(`/tasks/${taskId}/comments?userId=${currentEmployeeId}`);
        if (!response.ok) throw new Error('Failed to fetch comments');
        const comments = await response.json();

        if (comments.length === 0) {
            list.innerHTML = '<p class="text-sm" style="text-align: center;">No comments yet.</p>';
            return;
        }

        list.innerHTML = comments.map(c => `
            <div class="comment-item">
                <div class="comment-author">User ${c.authorId} &bull; ${new Date(c.createdAt).toLocaleString()}</div>
                <div>${c.content}</div>
            </div>
        `).join('');

    } catch (error) {
        list.innerHTML = '<p class="text-sm text-danger">Error loading comments.</p>';
    }
}

async function handleAddComment() {
    const content = document.getElementById('newCommentContent').value;
    if (!content.trim()) return;

    try {
        const params = new URLSearchParams();
        params.append('authorId', currentEmployeeId);
        params.append('content', content);

        const response = await fetch(`/tasks/${currentOpenTaskId}/comments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        });

        if (!response.ok) throw new Error('Failed to add comment');

        document.getElementById('newCommentContent').value = '';
        fetchComments(currentOpenTaskId); // Refresh comments

    } catch (error) {
        alert('Error adding comment');
    }
}

/* ---------------- UI Utils ---------------- */

function openModal(modalId) {
    document.getElementById(modalId).classList.add('open');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('open');
}
