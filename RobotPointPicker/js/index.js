import history from './position-history';
import { addKeybind } from "./keybinds";

let shouldSaveSettings = true;

const fieldZoom = {
    scale: 1.0,
    up: 0,
    left: 0,
}

const $robit = document.getElementById('robit');
const $field = document.getElementById('field-img');
const $reset_button = document.getElementById('reset');
const $sizing_square = document.getElementById('sizing-square');
const $inputs = document.querySelectorAll('input[type="text"]');
const $settings = extractSettingsFromDOM();

makeDraggable($sizing_square);

makeDraggable($robit, () => {
    $inputs.forEach(adjustWidthOfInput);
});

makeRobitRotatable();

['load', 'mousemove'].forEach(event => {
    $robit.addEventListener(event, updateXY);
});

['load', 'wheel'].forEach(event => {
    $robit.addEventListener(event, updateHeading);
});

$inputs.forEach(el => {
    adjustWidthOfInput(el);

    ['keyup', 'keydown'].forEach(event => {
        document.addEventListener(event, () => {
            adjustWidthOfInput(el);
        });
    });
});

$reset_button.onclick = resetSettings
window.addEventListener('beforeunload', saveSettings);

window.addEventListener('load', () => {
    const sizing_square_pos = JSON.parse(localStorage.getItem('sizing-square-pos'));
    $sizing_square.style.top = sizing_square_pos.top || '50%';
    $sizing_square.style.left = sizing_square_pos.left || '50%';
});

addKeybind('q', () => {
    $field.style.transform = `scale(${fieldZoom.scale -= .1})`;
    $robit.style.transform = `translate(-50%, -50%) scale(${fieldZoom.scale -= .1})`;
});

addKeybind('e', () => {
    $field.style.transform = `scale(${fieldZoom.scale += .1})`;
    $robit.style.transform = `translate(-50%, -50%) scale(${fieldZoom.scale += .1})`;
});

addKeybind('w', () => {
    $field.style.top = `${fieldZoom.up += 2}%`;
    $robit.style.top = `${50 + (fieldZoom.up)}%`;
});

addKeybind('a', () => {
    $field.style.left = `${fieldZoom.left += 2}%`;
    $robit.style.left = `${50 + (fieldZoom.left)}%`;
});

addKeybind('s', () => {
    $field.style.top = `${fieldZoom.up -= 2}%`;
    $robit.style.top = `${50 + (fieldZoom.up)}%`;
});

addKeybind('d', () => {
    $field.style.left = `${fieldZoom.left -= 2}%`;
    $robit.style.left = `${50 + (fieldZoom.left)}%`;
});

function saveSettings() {
    if (!shouldSaveSettings) return;

    localStorage.setItem('sizing-square-pos', JSON.stringify({
        top: $sizing_square.style.top,
        left: $sizing_square.style.left,
    }));
}

function resetSettings() {
    shouldSaveSettings = false;
    localStorage.clear();
    window.location.reload();
}

function adjustWidthOfInput(el) {
    let temp_span = document.createElement("h1");

    temp_span.className = "input-element tmp-element";
    temp_span.innerText = el.value

    document.body.appendChild(temp_span);
    let width = temp_span.getBoundingClientRect().width;
    document.body.removeChild(temp_span);

    el.style.width = width + "px";
}

function makeDraggable(el, onDrag) {
    let previousCursorX = 0, previousCursorY = 0

    el.addEventListener('mousedown', startDrag, false);
    window.addEventListener('mouseup', endDrag, false)

    function startDrag(e) {
        e.preventDefault();

        previousCursorX = e.clientX;
        previousCursorY = e.clientY;

        window.addEventListener('mousemove', drag, true);
    }

    function endDrag() {
        window.removeEventListener('mousemove', drag, true);
        onDrag && onDrag();
    }

    function drag(e) {
        e.preventDefault();

        const newElPosX = previousCursorX - e.clientX;
        const newElPosY = previousCursorY - e.clientY;

        previousCursorX = e.clientX;
        previousCursorY = e.clientY;

        el.style.top = (el.offsetTop - newElPosY) + 'px';
        el.style.left = (el.offsetLeft - newElPosX) + 'px';

        onDrag && onDrag();
    }
}

function makeRobitRotatable() {
    $robit.style.transformOrigin = 'center';

    $robit.addEventListener('wheel', e => {
        e.preventDefault();

        let increment = e.deltaY > 0 ? 1 : -1;
        if (e.shiftKey) increment *= .1;
        if (e.ctrlKey) increment *= 10;

        if ($robit.style.transform.includes('rotate')) {
            $robit.style.transform = $robit.style.transform.replace(/rotate\((\d*\.?\d*)deg\)/, (_, p1) => (
                `rotate(${(360 + (parseFloat(p1) + increment)) % 360}deg)`
            ));
        } else {
            $robit.style.transform = `translate(-50%, -50%) rotate(${(360 + increment) % 360}deg)`;
        }
    });
}

function updateXY() {
    const fieldWidth = $field.getBoundingClientRect().width;

    const robotCenterY = $robit.getBoundingClientRect().top + $robit.getBoundingClientRect().height * .5;
    const fieldCenterY = $field.getBoundingClientRect().top + $field.getBoundingClientRect().height * .5;

    const yDiff = (Math.abs(robotCenterY - fieldCenterY) < .01) ? 0 : robotCenterY - fieldCenterY;
    const yDiffToCm = (yDiff / fieldWidth) * -365.76; // Accounts for screen Y axis being flipped

    $settings.y.value = yDiffToCm.toFixed(3).replace(/^-0\.000$/, '0.000');


    const robotCenterX = $robit.getBoundingClientRect().left + $robit.getBoundingClientRect().width * .5;
    const fieldCenterX = $field.getBoundingClientRect().left + $field.getBoundingClientRect().width * .5;

    const xDiff = Math.abs(robotCenterX - fieldCenterX) < .01 ? 0 : robotCenterX - fieldCenterX;
    const xDiffToCm = (xDiff / fieldWidth) * 365.76;

    $settings.x.value = xDiffToCm.toFixed(3).replace(/^-0\.000$/, '0.000');


    $settings.x.style.color = parseInt($settings.x.value, 10) >= 0 ? '#51ad6a' : '#b54855';
    $settings.y.style.color = parseInt($settings.y.value, 10) >= 0 ? '#51ad6a' : '#b54855';
}

function updateHeading() {
    const rawHeading = $robit.style.transform.match(/rotate\((\d*\.?\d*)deg\)/)?.[1] || 0;
    const unitCircleHeading = (360 - (parseFloat(rawHeading) + 270) % 360) % 360; // Shut up I know this is inefficient, but it works, ok?
    $settings.heading.innerText = unitCircleHeading.toFixed(1);
}

function extractSettingsFromDOM() {
    const settings = {};
    document.querySelectorAll('*[id^="setting"]').forEach(el => {
        settings[el.id.slice(el.id.indexOf('-') + 1)] = el;
    });
    return settings;
}