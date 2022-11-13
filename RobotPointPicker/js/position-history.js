const STARTING_POSITION = { x: 0, y: 0, heading: 0 };

const positionHistory = [STARTING_POSITION];
let currentPosition = 0;

export default {
    addPosition: (position) => {
        positionHistory.push(position);
        positionHistory.splice(++currentPosition);
    },

    restorePreviousPosition: () => {
        if (currentPosition > 0) {
            currentPosition--;
        }
        return positionHistory[currentPosition];
    },

    restoreNextPosition: () => {
        if (currentPosition < positionHistory.length - 1) {
            currentPosition++;
        }
        return positionHistory[currentPosition];
    }
}
