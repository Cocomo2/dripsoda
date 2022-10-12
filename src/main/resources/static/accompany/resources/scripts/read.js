const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);
const coverImage = window.document.getElementById('coverImage');
const title = window.document.getElementById('title');
const region = window.document.getElementById('region');
const capacity = window.document.getElementById('capacity');
const dateFrom = window.document.getElementById('dateFrom');
const dateTo = window.document.getElementById('dateTo');
const content = window.document.getElementById('content');

cover.show('요청한 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.');
const xhr = new XMLHttpRequest();
xhr.open('POST', window.location.href);
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        cover.hide();
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseJson = JSON.parse(xhr.responseText);
            const dateFromObj = new Date(responseJson['dateFrom']);
            const dateToObj = new Date(responseJson['dateTo']);
            window.document.title = `${responseJson['title']} :: 드립소다`;
            coverImage.setAttribute('src', `../cover-image/${id}`);
            title.innerText = responseJson['title'];
            region.innerText = responseJson['regionValue'];
            capacity.innerText = `${responseJson['capacity']}명`;
            dateFrom.innerText = `${dateFromObj.getFullYear()}-${dateFromObj.getMonth() + 1}-${dateFromObj.getDate()}`;
            dateTo.innerText = `${dateToObj.getFullYear()}-${dateToObj.getMonth() + 1}-${dateToObj.getDate()}`;
            content.innerHTML = responseJson['content'];
        } else if (xhr.status === 404) {
            alert('존재하지 않는 동행 게시글입니다.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        }
    }
};
xhr.send();
