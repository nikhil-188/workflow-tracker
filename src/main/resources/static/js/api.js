// api.js - Handles all backend communication

const API_BASE = '/tasks';

/**
 * Fetch tasks created by a manager (Returns Summary JSON)
 */
async function getManagerTasks(managerId) {
    const response = await fetch(`${API_BASE}/manager/${managerId}`);
    if (!response.ok) throw new Error('Failed to fetch tasks');
    return await response.json();
}

/**
 * Fetch Full Task Details by ID
 */
async function getTaskDetails(taskId) {
    const response = await fetch(`${API_BASE}/${taskId}`);
    if (!response.ok) throw new Error('Failed to fetch task details');
    return await response.json();
}

/**
 * Fetch tasks assigned to an employee
 */
async function getEmployeeTasks(employeeId) {
    const response = await fetch(`${API_BASE}/employee/${employeeId}`);
    if (!response.ok) throw new Error('Failed to fetch tasks');
    return await response.json();
}

/**
 * Create a new task (Manager only)
 */
async function createTask(taskData) {
    const params = new URLSearchParams();
    for (const key in taskData) {
        params.append(key, taskData[key]);
    }

    const response = await fetch(API_BASE, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    });

    if (!response.ok) throw new Error('Failed to create task');
    return await response.json();
}

/**
 * Fetch comments for a specific task
 */
async function getComments(taskId, userId) {
    console.log(`API: Fetching comments for Task=${taskId} User=${userId}`);
    try {
        const response = await fetch(`${API_BASE}/${taskId}/comments?userId=${userId}`);

        if (!response.ok) {
            console.error(`API Error: ${response.status} ${response.statusText}`);
            const text = await response.text();
            console.error('API Error Body:', text);
            throw new Error('Failed to fetch comments');
        }

        const data = await response.json();
        console.log('API: Comments received:', data);
        return data;
    } catch (error) {
        console.error("API Network/Logic Error:", error);
        return []; // Return empty array so UI doesn't crash
    }
}

/**
 * Add a comment to a task
 */
async function addComment(taskId, userId, content) {
    const params = new URLSearchParams();
    params.append('authorId', userId);
    params.append('content', content);

    const response = await fetch(`${API_BASE}/${taskId}/comments`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    });

    if (!response.ok) throw new Error('Failed to add comment');
    return await response.json();
}

// Export functions to global scope (simple module pattern)
window.API = {
    getManagerTasks,
    getEmployeeTasks,
    getTaskDetails, // New method
    createTask,
    getComments,
    addComment
};
