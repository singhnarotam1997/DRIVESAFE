function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   a1, y, lambda)
                               
                               
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(a1, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
z2=a1*Theta1';
u=1./(1+exp(-z2));
a2=[ones(size(z2,1),1) u];
z3=a2*Theta2';
a3=1./(1+exp(-z3));
if num_labels==1
    %for singal class classification...
    y_matrix=y;
else
    %for multi-class classification...
    eye_matrix = eye(num_labels);
    y_matrix = eye_matrix(y,:);
end
J = (1/m)*(sum(sum((-y_matrix.*log(a3) - (1-y_matrix).*log(1-a3)))));
tempt1=Theta1;tempt1(:,1)=0;
tempt2=Theta2;tempt2(:,1)=0;

J=J+(lambda/(2*m))*(sum(sum(tempt1.^2))+sum(sum(tempt2.^2)));


d3=a3-y_matrix;
d2=(d3*Theta2(:,2:end)).*(sigmoidGradient(z2));
Delta1=d2'*a1;
Delta2=d3'*a2;
Theta1_grad=(1/m)*Delta1+(lambda/m)*tempt1;
Theta2_grad=(1/m)*Delta2+(lambda/m)*tempt2;

% 
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%



















% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];
clearvars -except J grad nn_params  input_layer_size hidden_layer_size num_labels a1 y lambda

end
