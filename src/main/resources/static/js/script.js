let inputTimeout;

function addValue(value) {
    let display = document.getElementById('nums');
    if(display.value.charAt(display.value.length - 1) === '.' && value === '.') {
        clearTimeout(inputTimeout);
    }
    if(display.value.charAt(display.value.length - 1) !== ' ' && value === '-') {
        clearTimeout(inputTimeout);
    }
    if((display.value.charAt(display.value.length - 1) === ' '
        || display.value.charAt(display.value.length - 1) === '-')
        && value === '.') {
        display.value = display.value + '0' + value;
    } else {
        display.value = display.value + value;
    }
    clearTimeout(inputTimeout);
    inputTimeout = setTimeout(function() {
        if(display.value.charAt(display.value.length - 1) === '.') {
            display.value = display.value + '0 ';
        } else {
            display.value = display.value + ' ';
        }
    }, 1500);
}

function addOperator(value) {
    clearField('operator')
    let display = document.getElementById('operator');
    display.value = display.value + value;
}

function clearField(fieldId) {
    document.getElementById(fieldId).value = '';
}

function removeSym() {
    let numsField = document.getElementById('nums');
    let currentNums = numsField.value;

    if (currentNums.length > 0) {
        if(currentNums.charAt(currentNums.length - 1) === ' ') {
            numsField.value = currentNums.slice(0, -2);
        } else {
            numsField.value = currentNums.slice(0, -1);
        }
    }
}