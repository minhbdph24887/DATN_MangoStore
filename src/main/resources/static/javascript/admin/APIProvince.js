document.addEventListener('DOMContentLoaded', function () {
    const apiAddressElements = document.querySelectorAll('.apiAddress');
    if (apiAddressElements.length > 0) {
        apiAddressElements.forEach(apiAddressElement => {
            const citis = apiAddressElement.querySelector(".city-select");
            const districts = apiAddressElement.querySelector(".district-select");
            const wards = apiAddressElement.querySelector(".ward-select");

            var Parameter = {
                url: "/api/address",
                method: "GET",
            };
            var promise = axios(Parameter);
            promise.then(function (result) {
                renderCity(result.data, citis, districts, wards);
            });

            function renderCity(data, citis, districts, wards) {
                for (const x of data) {
                    citis.options[citis.options.length] = new Option(x.Name, x.Name);
                }
                citis.onchange = function () {
                    districts.length = 1;
                    wards.length = 1;
                    if (this.value != "") {
                        const result = data.filter(n => n.Name === this.value);

                        for (const k of result[0].Districts) {
                            districts.options[districts.options.length] = new Option(k.Name, k.Name);
                        }
                    }
                    citis.value = this.value;
                };
                districts.onchange = function () {
                    wards.length = 1;
                    const dataCity = data.filter((n) => n.Name === citis.value);
                    if (this.value != "") {
                        const dataWards = dataCity[0].Districts.filter(n => n.Name === this.value)[0].Wards;

                        for (const w of dataWards) {
                            wards.options[wards.options.length] = new Option(w.Name, w.Name);
                        }
                    }
                    districts.value = this.value;
                };

                wards.onchange = function () {
                    wards.value = this.value;
                };
            }
        });
    }
});
