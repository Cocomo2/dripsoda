const registerForm = window.document.getElementById('registerForm');
const registerWarning = {
    getElement: () => window.document.getElementById('registerWarning'),
    hide: () => registerWarning.getElement().classList.remove('visible'),
    show: (text) => {
        registerWarning.getElement().innerText = text;
        registerWarning.getElement().classList.add('visible');
    }
};
const functions = {
    checkContactAuthCode: params => {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('contact', registerForm['contact'].value);
        formData.append('salt', registerForm['contactAuthSalt'].value);
        formData.append('code', registerForm['contactAuthCode'].value);
        xhr.open('POST', './contactAuthCode');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            alert('연락처가 성공적으로 인증되었습니다.');
                            registerForm['contactAuthCode'].setAttribute('disabled', 'disabled');
                            break;
                        case 'failure_expired':
                            alert('해당 인증 번호가 만료되었습니다.\n\n처음부터 다시 시도해 주세요.');
                            break;
                        default:
                            alert('인증 번호가 올바르지 않습니다.\n\n다시 한 번 확인해 주세요.');
                            registerForm['contactAuthCode'].focusAndSelect();
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다.\n\n잠시후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    },

    requestContactAuthCode: params => {
        if (registerForm['contact'].value === '') {
            registerWarning.show('연락처를 입력해 주세요.');
            registerForm['contact'].focus();
            return;
        }
        if (!new RegExp('^(\\d{8,12})$').test(registerForm['contact'].value)) {
            registerWarning.show('올바른 연락처를 입력해 주세요.');
            registerForm['contact'].focusAndSelect();
            return false;
        }
        cover.show('인증번호를 전송하고 있습니다.\n\n잠시만 기다려 주세요.');

        const xhr = new XMLHttpRequest();
        xhr.open('GET', `./contactAuthCode?contact=${registerForm['contact'].value}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            registerWarning.show('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다.');
                            registerForm['contactAuthSalt'].value = responseJson['salt'];
                            registerForm['contact'].setAttribute('disabled','disabled');
                            registerForm['contactAuthRequestButton'].setAttribute('disabled','disabled');
                            registerForm['contactAuthCode'].removeAttribute('disabled');
                            registerForm['contactAuthCheckButton'].removeAttribute('disabled');
                            registerForm['contactAuthCode'].focusAndSelect();
                            break;
                        default:
                            registerWarning.show('알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                            registerForm['contact'].focusAndSelect();
                    }
                } else {
                    registerWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    registerForm['contact'].focusAndSelect();
                }
            }
        };
        xhr.send();
    }
};

window.document.body.querySelectorAll('[data-func]').forEach(x => {
    x.addEventListener('click', e => {
        if (typeof(functions[x.dataset.func]) === 'function') {
            functions[x.dataset.func]({
                element: x,
                event: e
            });
        }
    });
});

registerForm.onsubmit = e => {
    e.preventDefault();
    registerWarning.hide();
    if (registerForm['email'].value === '') {
        registerWarning.show('이메일 주소를 입력해 주세요.')
        registerForm['email'].focus();
        return false;
    }
    if (!new RegExp('^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$').test(registerForm['email'].value)) {
        registerWarning.show('올바른 이메일 주소를 입력해 주세요.');
        registerForm['email'].focusAndSelect();
        return false;
    }
    if (registerForm['password'].value === '') {
        registerWarning.show('비밀번호를 입력해 주세요.');
        registerForm['password'].focus();
        return false;
    }
    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{8,50})$').test(registerForm['password'].value)) {
        registerWarning.show('올바른 비밀번호를 입력해 주세요.');
        registerForm['password'].focusAndSelect();
        return false;
    }
    if (registerForm['passwordCheck'].value === '') {
        registerWarning.show('비밀번호를 다시 한 번 더 입력해 주세요.');
        registerForm['passwordCheck'].focus();
        return false;
    }
    if (registerForm['password'].value !== registerForm['passwordCheck'].value) {
        registerWarning.show('입력한 비밀번호가 서로 일치하지 않습니다.');
        registerForm['passwordCheck'].focusAndSelect();
        return false;
    }
    if (registerForm['name'].value === '') {
        registerWarning.show('이름을 입력해 주세요.');
        registerForm['name'].focus();
        return false;
    }
    if (!new RegExp('^([가-힣]{2,5})$').test(registerForm['name'].value)) {
        registerWarning.show('올바른 이름을 입력해 주세요.');
        registerForm['name'].focusAndSelect();
        return false;
    }
    if (registerForm['contact'].value === '') {
        registerWarning.show('연락처를 입력해 주세요.');
        registerForm['contact'].focus();
        return false;
    }
    if (!new RegExp('^(\\d{8,12})$').test(registerForm['contact'].value)) {
        registerWarning.show('올바른 연락처를 입력해 주세요.');
        registerForm['contact'].focusAndSelect();
        return false;
    }
    if (!registerForm['contactAuthRequestButton'].disabled) {
        registerWarning.show('연락처를 인증해 주세요.');
        registerForm['contact'].focusAndSelect();
        return false;
    }
    if (!registerForm['contactAuthCheckButton'].disabled) {
        registerWarning.show('연락처 인증을 마무리해 주세요.');
        registerForm['contactAuthCode'].focusAndSelect();
        return false;
    }
    if (registerForm['contactAuthSalt'].value === '') {
        registerWarning.show('연락처 인증이 정상적으로 완료되지 않았습니다.');
        return false;
    }
    if (!registerForm['policyTerms'].checked) {
        registerWarning.show('서비스 이용약관을 읽고 동의해 주세요.');
        registerForm['policyTerms'].focus();
        return false;
    }
    if (!registerForm['policyPrivacy'].checked) {
        registerWarning.show('개인정보 처리방침을 읽고 동의해 주세요.');
        registerForm['policyPrivacy'].focus();
        return false;
    }
};















