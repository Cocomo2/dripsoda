let continents = [];
let countries = [];
let regions = [];

const xhr = new XMLHttpRequest();
cover.show('지역 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.');
xhr.open('PATCH', './');
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        cover.hide();
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseJson = JSON.parse(xhr.responseText);
            continents = responseJson['accompanyContinents'];
            countries = responseJson['accompanyCountries'];
            regions = responseJson['accompanyRegions'];
            // 배너 어쩌냐 아마 바디 자식이면 될듯
            // 밑에 로그 지우고 디자인하고 탭 컨테이너 만들기
            // 무한 스크롤 안쓰고 페이징쓰기
            console.log('불러온 대륙 : ' + continents.length + '개');
            console.log('불러온 국가 : ' + countries.length + '개');
            console.log('불러온 지역 : ' + regions.length + '개');

            console.log('지역 중 대륙이 동아시아인 것만 뽑으면?');
            regions.filter(x => x['continentValue'] === 'EA')
                .forEach(region => {
                    console.log(`이름 : ${region['text']}`);
                });
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            window.history.back();
        }
    }
};
xhr.send();