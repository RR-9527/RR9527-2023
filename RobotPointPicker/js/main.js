// TODO: Refactor literally everything

const positionHistory = [{top: '0', left: '0', rotation: '90',}]
let currentPosition = 0;

const robot = document.getElementById('robit');
const field = document.getElementById('field-img');

['mousemove', 'load'].forEach(listener => {
    robot.addEventListener(listener, () => {
        document.getElementById('setting-x').innerText = ((innerWidth/2 - robot.getBoundingClientRect().left - robot.getBoundingClientRect().width/2)/((field.getBoundingClientRect().width - robot.getBoundingClientRect().width)/-365.76)).toFixed(3)
        document.getElementById('setting-y').innerText = ((innerHeight/2 - robot.getBoundingClientRect().top - robot.getBoundingClientRect().height/2)/((field.getBoundingClientRect().height - robot.getBoundingClientRect().height)/365.76)).toFixed(3)
        document.getElementById('setting-r').innerText = positionHistory[currentPosition].rotation

        document.getElementById('setting-x').style.color = parseInt(document.getElementById('setting-x').innerText, 10) >= 0 ? '#51ad6a' : '#b54855'
        document.getElementById('setting-y').style.color = parseInt(document.getElementById('setting-y').innerText, 10) >= 0 ? '#51ad6a' : '#b54855'
    }, false);

})

// Make the DIV element draggable:
dragElement(document.getElementById("robit"));
dragElement(document.getElementById("sizing-square"));

function dragElement(elmnt) {
    let pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
    elmnt.onmousedown = dragMouseDown;

    function dragMouseDown(e) {
        e.preventDefault();
        // get the mouse cursor position at startup:
        pos3 = e.clientX;
        pos4 = e.clientY;
        document.onmouseup = onDragDone;
        // call a function whenever the cursor moves:
        document.onmousemove = elementDrag;
    }

    function elementDrag(e) {
        e.preventDefault();

        // calculate the new cursor position:
        pos1 = pos3 - e.clientX;
        pos2 = pos4 - e.clientY;
        pos3 = e.clientX;
        pos4 = e.clientY;

        // set the element's new position:
        elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
        elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
    }

    function onDragDone() {
        // stop moving when mouse button is released:
        document.onmouseup = null;
        document.onmousemove = null;

        positionHistory[++currentPosition] = {
            top: elmnt.style.top.slice(0, -2),
            left: elmnt.style.left.slice(0, -2),
            rotation: '90',
        };

        positionHistory.splice(currentPosition + 1)
    }
}
