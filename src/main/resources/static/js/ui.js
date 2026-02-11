// ui.js - Handles DOM manipulation (Raw JSON Mode)

const UI = {
    // Render: Task List as JSON Boxes
    renderTasks: (tasks, containerId, onClickCallback) => {
        const container = document.getElementById(containerId);
        container.style.display = 'block'; // Reset grid if any

        if (!tasks || tasks.length === 0) {
            container.innerHTML = `<div style="padding:20px;">No tasks found (JSON is empty array []).</div>`;
            return;
        }

        container.innerHTML = tasks.map(task => `
            <div class="json-box">
                <pre>${JSON.stringify(task, null, 2)}</pre>
                <div style="margin-top:10px;">
                    <button class="btn btn-primary" onclick="window.triggerTaskClick(${task.id})">
                        Task Detail (JSON)
                    </button>
                    <!-- Visual Separator for clarity -->
                    <span style="font-size:0.8rem; color:#666; margin-left:10px;">ID: ${task.id}</span>
                </div>
            </div>
        `).join('');

        // Attach click handler to global scope
        window.triggerTaskClick = onClickCallback;
    },

    // Render: Comments (Already in JSON format inside modal usually, but helper here)
    renderComments: (comments, containerId) => {
        const container = document.getElementById(containerId);
        if (!comments || comments.length === 0) {
            container.innerHTML = '<pre>[] (No comments)</pre>';
            return;
        }
        container.innerHTML = `<pre>${JSON.stringify(comments, null, 2)}</pre>`;
    },

    // Modal: Open
    openModal: (modalId) => {
        const modal = document.getElementById(modalId);
        if (modal) modal.classList.add('visible');
    },

    // Modal: Close
    closeModal: (modalId) => {
        const modal = document.getElementById(modalId);
        if (modal) modal.classList.remove('visible');
    },

    // Utilities (kept for compatibility if needed, but unused in JSON mode)
    formatDate: (d) => d,
    getPriorityBadge: (p) => p
};

// Export to window
window.UI = UI;
