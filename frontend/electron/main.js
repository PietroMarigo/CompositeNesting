const { app, BrowserWindow } = require('electron');
const path = require('path');
const { spawn } = require('child_process');

let backendProcess;

function startBackend() {
  const jarPath = app.isPackaged
    ? path.join(process.resourcesPath, 'backend', 'backend.jar')
    : path.join(__dirname, '..', '..', 'backend', 'target', 'backend-0.0.1-SNAPSHOT.jar');

  backendProcess = spawn('java', ['-jar', jarPath], {
    stdio: 'inherit',
    cwd: app.isPackaged ? process.resourcesPath : undefined,
  });

  backendProcess.on('close', (code) => {
    console.log(`Backend exited with code ${code}`);
  });
}

function createWindow() {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
    },
  });

  if (app.isPackaged) {
    win.loadFile(path.join(__dirname, '..', 'dist', 'index.html'));
  } else {
    win.loadURL('http://localhost:5173');
  }
}

app.whenReady().then(() => {
  startBackend();
  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});

app.on('before-quit', () => {
  if (backendProcess) {
    backendProcess.kill();
  }
});
