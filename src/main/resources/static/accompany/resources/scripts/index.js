let continents = [];
let countries = [];
let regions = [];

const continentContainer = window.document.getElementById('continentContainer');

const drawContinents = () => {
    continentContainer.innerHTML = '';
    continents.forEach(continent => {
        const continentElement = window.document.createElement('div');
        continentElement.classList.add('continent');
        continentElement.dataset.value = continent['value'];
        continentElement.innerText = continent['text'];
        continentElement.addEventListener('click', e => {
            if (e.target.getAttribute('selected') === 'selected') {
                return;
            }
            drawSubs(continents.find(x => x['value'] === e.target.dataset.value));
            setSelectedContinent(e.target.dataset.value);
        });
        continentContainer.append(continentElement);
    });
    continentContainer
        .querySelector(':scope > .continent:first-of-type')
        .setAttribute('selected', 'selected');
};

const getSelectedContinent = () => {
    const selectedValue = continentContainer
        .querySelector(':scope > .continent[selected]')
        .dataset.value;
    return continents.find(x => x['value'] === selectedValue);
};

const setSelectedContinent = (value) => {
    continentContainer
        .querySelectorAll(':scope > .continent')
        .forEach(x => x.removeAttribute('selected'));
    continentContainer
        .querySelector(`:scope > .continent[data-value="${value}"]`)
        .setAttribute('selected', 'selected');
};

const subContainer = window.document.getElementById('subContainer');
const drawSubs = (continent) => {
    subContainer.innerHTML = '';
    countries
        .filter(x => x['continentValue'] === continent['value'])
        .forEach(country => {
            const subElement = window.document.createElement('div');
            subElement.classList.add('sub');
            const countryElement = window.document.createElement('div');
            countryElement.classList.add('country');
            countryElement.dataset.value = country['value'];
            countryElement.innerText = country['text'];
            countryElement.addEventListener('click', e => {
                setSelectedCountry(e.target.dataset.value);
            });

            const regionContainerElement = window.document.createElement('div');
            regionContainerElement.classList.add('region-container');
            regions
                .filter(x => x['continentValue'] === country['continentValue'] &&
                    x['countryValue'] === country['value'])
                .forEach(region => {
                    const regionElement = window.document.createElement('div');
                    regionElement.classList.add('region');
                    regionElement.dataset.value = region['value'];
                    regionElement.innerText = region['text'];
                    regionElement.addEventListener('click', e => {
                        if (e.target.getAttribute('selected') === 'selected') {
                            return;
                        }
                        setSelectedCountry(e.target.parentNode.parentNode.querySelector('.country[data-value]').dataset.value);
                        setSelectedRegion(e.target.dataset.value);
                    });
                    regionContainerElement.append(regionElement);
                });

            subElement.append(countryElement, regionContainerElement);
            subContainer.append(subElement);
        });
    subContainer
        .querySelector(':scope > .sub:first-of-type > .country')
        .setAttribute('selected', 'selected');
};

const getSelectedCountry = () => {
    const selectedContinent = getSelectedContinent();
    const selectedCountryElement = subContainer.querySelector('.country[data-value][selected]');
    return countries.find(x =>
        x['continentValue'] === selectedContinent['value'] &&
        x['value'] === selectedCountryElement.dataset.value);
};

const setSelectedCountry = (value) => {
    subContainer.querySelectorAll('.region[data-value]').forEach(x => x.removeAttribute('selected'));
    subContainer.querySelectorAll('.country[data-value]').forEach(x => x.removeAttribute('selected'));
    subContainer.querySelector(`.country[data-value="${value}"]`)?.setAttribute('selected','selected');
};

const setSelectedRegion = (value) => {
    subContainer.querySelectorAll('.region[data-value]').forEach(x => x.removeAttribute('selected'));
    const selectedCountryValue = getSelectedCountry()['value'];
    subContainer.querySelector(`.country[data-value=${selectedCountryValue}]`)
        .parentNode
        .querySelector(`.region[data-value="${value}"]`)
        .setAttribute('selected', 'selected');
};

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
            drawContinents();
            drawSubs(getSelectedContinent());
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            window.history.back();
        }
    }
};
xhr.send();
