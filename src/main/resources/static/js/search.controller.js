'use strict';
 
angular.module('kohonenApp').controller('SearchController', ['$scope', 'HttpService', function($scope, HttpService) {
    var self = this;
    self.image={};
    
    self.search = search;
 
    function search() {
        HttpService.search(self.image)
        .then(
                function(d) {
                    console.log('success');
                },
                function(errResponse){
                    console.error('Error while searching');
                }
            );
    }
 
}]);