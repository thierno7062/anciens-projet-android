/**
 * Created by admin on 18/12/2016.
 */

angular.module('petiteAnnonceKmerApp')
    .filter('pakDate',convertDate);

convertDate.$inject = ['$translate','$filter'];


Date.prototype.isToday = function() {
    var d = new Date();
    return this.getFullYear() === d.getFullYear()
        && this.getDate() === d.getDate()
        && this.getMonth() === d.getMonth();
}

Date.prototype.isYesterDay = function() {

    var d = new Date();
    d.setDate(d.getDate()-1);
    return this.getFullYear() === d.getFullYear()
        && this.getDate() === d.getDate()
        && this.getMonth() === d.getMonth();
}

Date.prototype.isCurrentYear = function() {
    var d = new Date();
    return this.getFullYear() === d.getFullYear();
}

Date.prototype.customMonth = function() {
    if (this.getMonth() == 0){return "January"};
    if (this.getMonth() == 1){return "February"};
    if (this.getMonth() == 2){return "March"};
    if (this.getMonth() == 3){return "April"};
    if (this.getMonth() == 4){return "May"};
    if (this.getMonth() == 5){return "June"};
    if (this.getMonth() == 6){return "July"};
    if (this.getMonth() == 7){return "August"};
    if (this.getMonth() == 8){return "September"};
    if (this.getMonth() == 9){return "October"};
    if (this.getMonth() == 10){return "November"};
    if (this.getMonth() == 11){return "December"};
};


function convertDate($translate,$filter) {
    return function(date) {
        var stringDate = '';

        if(date == null)
            return $translate.instant('NOTMENTIONNE');

        date = new Date(date);


        var hour = n(date.getHours()) + ":"
            + n(date.getMinutes());

        if(date.isToday())
        {
            stringDate = $translate.instant('TODAY')+', '+hour;
        } else if(date.isYesterDay())
        {
            stringDate = $translate.instant('YESTERDAY')+', '+hour;
        }else if(date.isCurrentYear())
        {
            stringDate = date.getDate()+' '+$translate.instant(date.customMonth())+', '+hour;
        }else{
            stringDate = $filter('date')(date, "dd/MM/yyyy HH:mm");
        }

        return stringDate;
    }

    function n(n){
        return n > 9 ? "" + n: "0" + n;
    }
}
