function expandTextarea(id) {
    document.getElementById(id).addEventListener('keyup', function() {
        this.style.overflow = 'hidden';
        this.style.height = 0;
        this.style.height = this.scrollHeight + 'px';
    }, false);
}

function updateSimilarDates(id) {
    document.getElementById(id).addEventListener('blur', function() {
        var text = this.value;
        var elements = document.getElementsByClassName('date-class');
        var dateComponents = text.split('-');
        var nonLocDate = new Date("20" + dateComponents[2],dateComponents[1] - 1,dateComponents[0]);
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
        document.getElementById("dateOfForm").value = formatter.format(nonLocDate);
    })
}

function updateSimilarCosts(id) {
    document.getElementById(id).addEventListener('blur', function() {
        var text = this.value;
        var elements = document.getElementsByClassName('cost-class');
        for (var i = 0; i < elements.length; i++) {
            var elementId = elements[i].getAttribute("id");
            if ((elementId != null) && (elementId != id)) {
                document.getElementById(elementId).value = text.replace(/\B(?=(\d{3})+(?!\d))/g, " ") + ", 00";
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

expandTextAreas();
updateSimilarDates('dealNumber');
updateSimilarCosts('cost');
