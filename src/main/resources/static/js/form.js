function expandTextarea(id) {
    document.getElementById(id).addEventListener('keyup', function() {
        this.style.overflow = 'hidden';
        this.style.height = 0;
        this.style.height = this.scrollHeight + 'px';
    }, false);
}

function isValidDate(d) {
    return d instanceof Date && !isNaN(d);
}

function checkDealNumber(dealNumber) {
    var result = true;

    if (((/[0-9]{2,2}-[0-9]{2,2}-[0-9]{2,2}/.test(dealNumber)) && (dealNumber.length == 8))) {
        return result;
    }

    if ((/[0-9]{2,2}-[0-9]{2,2}-[0-9]{2,2}-[0-9]{1,3}/.test(dealNumber)) && (dealNumber.length >= 10) && (dealNumber.length <= 12)) {
        var dateComponents = dealNumber.split('-');
        if (isNaN(dateComponents[3])) {
            result = false;
        }
    } else {
        result = false;
    }


    return result;
}
function updateSimilarDates(id) {
    document.getElementById(id).addEventListener('blur', function() {
        var text = this.value.trim();
        if (!(checkDealNumber(text))) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
            var elements = document.getElementsByClassName('date-class');
            var dateComponents = text.split('-');
            var nonLocDate = new Date("20" + dateComponents[2],dateComponents[1] - 1,dateComponents[0]);
            if (!isValidDate(nonLocDate)) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
                var formatter = new Intl.DateTimeFormat("ru", {
                    year: "numeric",
                    month: "long",
                    day: "numeric"
                });

                for (var i = 0; i < elements.length; i++) {
                    var elementId = elements[i].getAttribute("id");
                    if ((elementId != null) && (elementId != id)) {
                        document.getElementById(elementId).value = text;
                    }
                }

                document.getElementById("dealDate").value = formatter.format(nonLocDate);
                document.getElementById("billDate").value = formatter.format(nonLocDate);
                document.getElementById("actDate").value = formatter.format(nonLocDate);
                if (document.getElementById("dateOfForm").value == '')
                    document.getElementById("dateOfForm").value = formatter.format(nonLocDate);
            }
        }
    });
}

function updateDateofForm(id) {
    document.getElementById(id).addEventListener('blur', function() {
        var text = this.value.trim();
        var months = ['января','февраля','марта','апреля','мая','июня','июля','августа','сентября','октября','ноября','декабря'];
        var textParts = text.split(' ');
        if (!(textParts.length == 4) ) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');

            var isGoodDate = true;
            if ((parseInt(textParts[0]) >= 1) && (parseInt(textParts[0]) <= 31) && (parseInt(textParts[2]) >= 1900) && (parseInt(textParts[2]) <= 2200) ) {
                var month = months.indexOf(textParts[1]) ? months.indexOf(textParts[1]) + 1 : -1;

                if ((month >= 1) && (month <= 12)) {
                    if (textParts[3] == 'г.') {
                       if (isNaN(Date.parse(month + '/' + textParts[0] + '/' + textParts[2]))) {
                           isGoodDate = false;
                       }
                    } else {
                        isGoodDate = false;
                    }
                } else {
                    isGoodDate = false;
                }
            } else {
                isGoodDate = false;
            }
            if (isGoodDate) {
                document.getElementById("dateOfForm").value = text;
            } else {
                this.classList.add('is-invalid');
            }
        }

    });

}

function updateSimilarCosts(id) {
    document.getElementById(id).addEventListener('blur', function() {
        var text = this.value.trim();
        if (!/^[0-9]*$/.test(text)) {
            this.classList.add('is-invalid');
            document.getElementById(id + 'Help').classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
            document.getElementById(id + 'Help').classList.remove('is-invalid');
            var elements = document.getElementsByClassName('cost-class');
            for (var i = 0; i < elements.length; i++) {
                var elementId = elements[i].getAttribute("id");
                if ((elementId != null) && (elementId != id)) {
                    document.getElementById(elementId).value = text.replace(/\B(?=(\d{3})+(?!\d))/g, " ") + ", 00";
                }
            }
        }
    });
}

function expandTextAreas() {
    var elemlist = document.getElementsByTagName('textarea');
    for (var i = 0; i < elemlist.length; i++) {
        var id = elemlist[i].getAttribute('id');
        if ((id != null) & (id != undefined)) {
            expandTextarea(id);
        }
    }
}

$(document).ready(function() {
    $('.needs-validation').submit(function (event) {
        var invalidElem = document.getElementsByClassName('is-invalid');
        if (invalidElem.length > 0) {
            event.preventDefault();
            $('#failure-modal').modal('toggle');
            return false;
        } else {
            return true;
        }
    });

    $('.back-to-settings').on('click', function(event){
        event.stopPropagation();
        event.stopImmediatePropagation();
        $('#success-modal').modal('hide');
        $('#failure-modal').modal('hide'); // rewrite this using parents!
    });

});
expandTextAreas();
//updateSimilarDates('dealNumber');
//updateDateofForm('dealDate');
updateSimilarCosts('cost');

