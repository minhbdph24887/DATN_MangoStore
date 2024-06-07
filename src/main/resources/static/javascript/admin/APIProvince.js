const apiAddressElement = document.querySelector('.apiAddress');
if (apiAddressElement) {
    var citis = document.getElementById("city");
    var districts = document.getElementById("district");
    var wards = document.getElementById("ward");
    var Parameter = {
        url: "/api/address",
        method: "GET",
    };
    var promise = axios(Parameter);
    promise.then(function (result) {
        renderCity(result.data);
    });

    function renderCity(data) {
        for (const x of data) {
            citis.options[citis.options.length] = new Option(x.Name, x.Name); // Sửa ở đây
        }
        citis.onchange = function () {
            district.length = 1;
            ward.length = 1;
            if (this.value != "") {
                const result = data.filter(n => n.Name === this.value); // Sửa ở đây

                for (const k of result[0].Districts) {
                    district.options[district.options.length] = new Option(k.Name, k.Name); // Sửa ở đây
                }
            }
            citis.value = this.value;
        };
        district.onchange = function () {
            ward.length = 1;
            const dataCity = data.filter((n) => n.Name === citis.value); // Sửa ở đây
            if (this.value != "") {
                const dataWards = dataCity[0].Districts.filter(n => n.Name === this.value)[0].Wards; // Sửa ở đây

                for (const w of dataWards) {
                    wards.options[wards.options.length] = new Option(w.Name, w.Name); // Sửa ở đây
                }
            }
            districts.value = this.value;
        };

        wards.onchange = function () {
            wards.value = this.value;
        };
    }
}
