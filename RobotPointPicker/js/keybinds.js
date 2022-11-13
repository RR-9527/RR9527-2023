const keybinds = {};

export function addKeybind(key, callback) {
    keybinds[key] = callback;
}

document.addEventListener('keypress', e => {
    if (keybinds[e.key]) {
        keybinds[e.key]();
    }
})
