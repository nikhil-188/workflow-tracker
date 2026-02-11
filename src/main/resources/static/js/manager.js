// Global State
let currentManagerId = localStorage.getItem('userId');
let currentOpenTaskId = null;

// Auth Check
if (!currentManagerId || localStorage.getItem('role') !== 'MANAGER') {
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
        const response = await fetch(`/tasks/manager/${currentManagerId}`);
        if (!response.ok) throw new Error('Failed to fetch tasks');
        const tasks = await response.json();
        renderTaskList(tasks);
    } catch (error) {
        console.error(error);
        document.getElementById('taskList').innerHTML =
            `<div class="card" style="text-align: center; color: var(--danger); grid-column: 1/-1;">Error loading tasks. Is the backend running?</div>`;
    }
}

async function handleCreateTask(event) {
    event.preventDefault();

    const title = document.getElementById('createTitle').value;
    const description = document.getElementById('createDescription').value;
    const priority = document.getElementById('createPriority').value;
    const dueDate = document.getElementById('createDueDate').value;
    const assignedTo = document.getElementById('createAssignedTo').value;

    try {
        const params = new URLSearchParams();
        params.append('title', title);
        params.append('description', description);
        params.append('priority', priority.toUpperCase()); // Ensure uppercase for Enum
        params.append('assignedToUserId', assignedTo);
        params.append('dueDate', dueDate);

        // Note: The backend currently hardcodes managerId=1 in the Controller, 
        // but we are simulating 'logged in as manager 1' so it matches.

        const response = await fetch('/tasks', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        });

        if (!response.ok) throw new Error('Failed to create task');

        closeModal('createTaskModal');
        event.target.reset();
        fetchTasks(); // Refresh list

    } catch (error) {
        alert('Error creating task: ' + error.message);
    }
}

async function openTaskDetails(taskId) {
    currentOpenTaskId = taskId;
    // We already have the list, but let's fetch individual details if needed
    // Or just find it from the list we (could have) stored. 
    // For simplicity, let's re-fetch the list or pass data. 
    // Actually, backend 'getTaskById' might be missing based on context. 
    // So we will rely on what we have in the DOM or re-fetch list.
    // OPTIMIZATION: Just re-fetch list for now or use the clicked element data if we stored it.

    // Since we don't have getTaskById API exposed in the snippets I saw, 
    // we will cheat slightly and find it from the list we just fetched if we stored it.
    // But since I didn't store global state 'tasks', let's just refresh list and find it? No that's slow.
    // Let's attach data to the card element.

    // Wait, the user wants 'click task -> show info'.
    // I can pass the object to the render function.
}

// Since I can't pass objects easily in HTML onclick, I'll store tasks globally
let allTasks = [];

function renderTaskList(tasks) {
    allTasks = tasks;
    const container = document.getElementById('taskList');

    if (tasks.length === 0) {
        container.innerHTML = `<div class="card" style="text-align: center; grid-column: 1/-1;">No tasks created yet.</div>`;
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
    document.getElementById('detailAssignedTo').innerText = `${task.assignedTo.name} (ID: ${task.assignedTo.id})`;

    // Fetch Comments
    fetchComments(taskId);

    openModal('taskDetailsModal');
}

/* ---------------- Comments ---------------- */

async function fetchComments(taskId) {
    const list = document.getElementById('commentsList');
    list.innerHTML = '<p class="text-sm">Loading comments...</p>';

    try {
        const response = await fetch(`/tasks/${taskId}/comments?userId=${currentManagerId}`);
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
        params.append('authorId', currentManagerId);
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

function openCreateTaskModal() {
    openModal('createTaskModal');
}
