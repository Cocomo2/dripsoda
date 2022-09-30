const infoForm = window.document.getElementById('infoForm');

const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
};

infoForm?.changePassword?.addEventListener('input', () => {
    infoForm.querySelectorAll('[rel="row-change-password"]').forEach(x => {
        if (infoForm['changePassword'].checked) {
            x.classList.add('visible');
            infoForm['newPassword'].value = '';
            infoForm['newPasswordCheck'].value = '';
            infoForm['newPassword'].focus();
        } else {
            x.classList.remove('visible');
        }
    });
});

infoForm?.changenewContact?.addEventListener('input', () => {
    infoForm.querySelectorAll('[rel="row-change-newContact"]').forEach(x => {
        if (infoForm['changeContact'].checked) {
            x.classList.add('visible');
            infoForm['newContactAuthSalt'].value = '';
            infoForm['newContact'].value = '';
            infoForm['newContact'].removeAttribute('disabled');
            infoForm['newContactAuthRequestButton'].removeAttribute('disabled');
            infoForm['newContactAuthCode'].value = '';
            infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
            infoForm['newContactAuthCheckButton'].setAttribute('disabled', 'disabled');
            infoForm['newContact'].focus();
        } else {
            x.classList.remove('visible');
        }
    });
});

infoForm?.newnewContactAuthRequestButton.addEventListener('click', ()=> {
    warning.hide();
    if(infoForm['newContact'].value === '') {
        warning.show('새로운 연락처를 입력해 주세요.');
        infoForm['newContact'].focus()
        return;
    }
    if (!new RegExp('^(\\d{8,12})$').test(infoForm['newnewContact'].value)) {
        warning.show('올바른 연락처를 입력해 주세요.');
        infoForm['newContact'].focusAndSelect();
        return;
    }
    cover.show('인증번호를 전송하고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `./userMyInfoAuth?newnewContact=${infoForm['newnewContact'].value}`);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'duplicate':
                        warning.show('해당 연락처는 이미 사용 중입니다.');
                        infoForm['newnewContact'].focusAndSelect();
                        break;
                    case 'success':
                        warning.show('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다.\n 5분 내로 문자로 전송된 인증번호를 확인해 주세요.');
                        infoForm['newContactAuthSalt'].value = responseJson['salt'];
                        infoForm['newContact'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthRequestButton'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthCode'].removeAttribute('disabled');
                        infoForm['newContactAuthCheckButton'].removeAttribute('disabled');
                        infoForm['newContactAuthCode'].focusAndSelect();
                        break;
                    default:
                        warning.show('알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                        infoForm['newContact'].focusAndSelect();
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                infoForm['newContact'].focusAndSelect();
            }
        }
    };
    xhr.send();
    
});


infoForm?.newContactAuthCheckButton.addEventListener('click', ()=> {
    warning.hide();
    if (infoForm['newContactAuthCode'].value === '') {
        warning.show('인증번호를 입력해 주세요.');
        infoForm['newContactAuthCode'].focus();
        return;
    }
    if (!new RegExp('^(\\d{6})$').test(infoForm['newContactAuthCode'].value)) {
        warning.show('올바른 인증번호를 입력해 주세요.');
        infoForm['newContactAuthCode'].focusAndSelect();
        return;
    }
    cover.show('인증번호를 확인하고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('newContact', infoForm['newContact'].value);
    formData.append('newContactAuthCode', infoForm['newContactAuthCode'].value);
    formData.append('newContactAuthSalt', infoForm['newContactAuthSalt'].value);
    xhr.open('POST', './userMyInfoAuth');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'expired':
                        warning.show('입력한 인증번호가 만료되었습니다. 인증번호를 다시 요청하여 인증해 주세요.');
                        infoForm['newContact'].removeAttribute('disabled');
                        infoForm['newContactAuthRequestButton'].removeAttribute('disabled');
                        infoForm['newContactAuthCode'].value = '';
                        infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthCheckButton'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthSalt'].value = '';
                        infoForm['newContact'].focusAndSelect();
                        break;
                    case 'success':
                        warning.show('연락처가 성공적으로 인증되었습니다.');
                        infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthCheckButton'].setAttribute('disabled', 'disabled');
                        break;
                    default:
                        infoForm['newContactAuthCode'].focusAndSelect();
                        warning.show('입력한 인증번호가 올바르지 않습니다.');
                }
            } else {
                registerWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                registerForm['newContactAuthCode'].focusAndSelect();
            }
        }
    };
    xhr.send(formData);

});



























