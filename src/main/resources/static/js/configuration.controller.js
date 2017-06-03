'use strict';
 
angular.module('kohonenApp').controller('ConfigurationController', ['$scope', 'HttpService', function($scope, HttpService) {
    var self = this;
    self.configuration={rows:'',cols:'',epochs:'',learningRate:'',radius:''};
    
    self.submit = submit;
 
    function submit() {
        HttpService.configureNetwork(self.configuration)
        .then(
                function(d) {
                    console.log('success');
                },
                function(errResponse){
                    console.error('Error while setting configuration');
                }
            );
    }
 
}]);