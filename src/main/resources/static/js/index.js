function loginAsManager() {
    // Hardcoded for demo purposes
    localStorage.setItem('userId', '1');
    localStorage.setItem('role', 'MANAGER');
    window.location.href = 'manager.html';
}

function loginAsEmployee() {
    // Hardcoded for demo purposes
    localStorage.setItem('userId', '2'); // Assuming Employee ID 2 exists in DB
    localStorage.setItem('role', 'EMPLOYEE');
    window.location.href = 'employee.html';
}