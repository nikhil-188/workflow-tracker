// app.js - Main Application Logic
// Depends on: api.js, ui.js

const App = {
    user: {
        id: localStorage.getItem('userId'),
        role: localStorage.getItem('role')
    },

    init: () => {
        if (!App.user.id) {
            // Check if we are on index.html, if so, do nothing
            if (!window.location.pathname.includes('index.html') && window.location.pathname !== '/') {
                window.location.href = 'index.html';
            }
            return;
        }

        // Logout handler
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) logoutBtn.addEventListener('click', App.logout);

        // Detect Page
        const path = window.location.pathname;
        if (path.includes('manager')) {
            if (App.user.role !== 'MANAGER') window.location.href = 'index.html';
            App.initManager();
        } else if (path.includes('employee')) {
            if (App.user.role !== 'EMPLOYEE') window.location.href = 'index.html';
            App.initEmployee();
        }
    },

    initManager: async () => {
        try {
            const tasks = await API.getManagerTasks(App.user.id);
            UI.renderTasks(tasks, 'taskList', App.handleTaskClick);
        } catch (err) {
            console.error(err);
        }
    },

    initEmployee: async () => {
        try {
            const tasks = await API.getEmployeeTasks(App.user.id);
            UI.renderTasks(tasks, 'taskList', App.handleTaskClick);
        } catch (err) {
            console.error(err);
        }
    },

    // Handlers
    handleTaskClick: async (taskId) => {
        App.currentOpenTaskId = taskId;

        document.getElementById('modalTitle').innerText = `Task ID: ${taskId} (Loading...)`;

        // 1. Fetch FULL Details from Backend (since list only has summary)
        let task = null;
        try {
            task = await API.getTaskDetails(taskId);
        } catch (e) {
            console.error("Failed to load task details", e);
            alert("Failed to load task details");
            return;
        }

        // 2. Prepare JSON Data Container
        const fullDetails = {
            task: task,
            comments: 'Loading...'
        };

        // 3. Helper to update the JSON View
        const updateJsonView = (data) => {
            document.getElementById('modalDesc').innerHTML = `<pre style="background:#f4f4f4; padding:10px; border:1px solid #ccc; max-height:400px; overflow:auto;">${JSON.stringify(data, null, 2)}</pre>`;
        };

        // 4. Initial Render
        document.getElementById('modalTitle').innerText = `Task ID: ${task.id} (JSON Mode)`;
        updateJsonView(fullDetails);
        document.getElementById('modalMeta').innerHTML = ''; // Hide standard meta
        document.getElementById('modalCommentsList').style.display = 'none'; // Hide standard comment list

        // 5. Fetch Comments and Update JSON
        try {
            const comments = await API.getComments(taskId, App.user.id);
            fullDetails.comments = comments;
            updateJsonView(fullDetails);
        } catch (e) {
            console.error(e);
            fullDetails.comments = "Error loading comments";
            updateJsonView(fullDetails);
        }

        UI.openModal('taskDetailModal');
    },

    // Create Task (Manager)
    handleCreateSubmit: async (e) => {
        e.preventDefault();
        const data = {
            title: document.getElementById('createTitle').value,
            description: document.getElementById('createDesc').value,
            priority: document.getElementById('createPriority').value,
            dueDate: document.getElementById('createDate').value,
            assignedToUserId: document.getElementById('createAssignee').value
        };

        try {
            await API.createTask(data);
            UI.closeModal('createTaskModal');
            e.target.reset(); // Clear form
            App.initManager(); // Refresh list
        } catch (err) {
            alert('Error creating task: ' + err.message);
        }
    },

    // Add Comment
    handleCommentSubmit: async () => {
        const input = document.getElementById('newCommentInput');
        const content = input.value;
        if (!content.trim()) return;

        try {
            await API.addComment(App.currentOpenTaskId, App.user.id, content);
            input.value = '';
            // Refresh comments
            const comments = await API.getComments(App.currentOpenTaskId, App.user.id);
            UI.renderComments(comments, 'modalCommentsList');
        } catch (err) {
            alert('Failed to add comment');
        }
    },

    logout: () => {
        localStorage.clear();
        window.location.href = 'index.html';
    }
};

// Initialize on Load
document.addEventListener('DOMContentLoaded', () => {
    App.init();

    // Attach Global Listeners that don't depend on specific pages
    window.App = App; // Expose for inline onclicks if needed, though we try to avoid them
});
